package org.ndm.imageproc;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

public class ThreaddedImageProcessor implements ImageProcessor{

	private ExecutorService executorService = null;
	private Integer numThreads = null;
	
	public ThreaddedImageProcessor(){}
	
	public ThreaddedImageProcessor(Integer numThreads){
		init(numThreads);
	}
	
	private void init(Integer numThreads){
		// Set a hint to process the imagery in 512x512 pixel chunks
		ImageLayout layout = new ImageLayout();
		layout.setTileWidth(512);
		layout.setTileHeight(512);
		JAI.getDefaultInstance().setRenderingHint(JAI.KEY_IMAGE_LAYOUT, layout);
		
		// Give 3/4 of the jvm ram to JAI for tile caching
		JAI.getDefaultInstance().getTileCache().setMemoryCapacity(Runtime.getRuntime().maxMemory()*3/4);
		
		// Create the executor service
		this.executorService = Executors.newFixedThreadPool(numThreads);
		this.numThreads = numThreads;
	}
	
	@Override
	public void process(ImageProcessingRecipe recipe) {
		
		processTiles(recipe.getRecipeAsJaiRenderedOp());
		
	}

	@Override
	public void processAndStore(ImageProcessingRecipe recipe, String outputFileName) {
		
		// Get the image associated with the recipe
		RenderedOp image = recipe.getRecipeAsJaiRenderedOp();
		
		// Process the tiles
		processTiles(recipe.getRecipeAsJaiRenderedOp());
		
		// Write the processed tiles to an image on disk
		try {
			System.out.println("Writing image to disk.");
			ImageIO.write(image, "tif", new File(outputFileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void processTiles(RenderedOp image){
		long start = System.currentTimeMillis();
		List<Future<Boolean>> tasks = new ArrayList<Future<Boolean>>();
		
		for(int y=image.getMinTileY(); y<=image.getMaxTileY(); y++){
			for(int x=image.getMinTileX(); x<=image.getMaxTileX(); x++){
				Future<Boolean> task = executorService.submit(new ProcessTileTask(image, x, y));
				tasks.add(task);
			}
		}
		
		for(Future<Boolean> task : tasks){
			try {
				task.get();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Processed "+tasks.size()+" image tiles in "+(end-start)+" ms with "+numThreads+" threads.");
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	private class ProcessTileTask implements Callable<Boolean>{
		private RenderedOp image = null;
		private Integer tileX = null;
		private Integer tileY = null;
		
		public ProcessTileTask(RenderedOp image, Integer tileX, Integer tileY){
			this.image = image;
			this.tileX = tileX;
			this.tileY = tileY;
		}
		
		@Override
		public Boolean call() throws Exception{
			// Triggers the rendering of an individual tile
			Raster tileRaster = image.getTile(tileX, tileY);
			return Boolean.TRUE;
		}
		
	}

	@Override
	public void shutdown() {
		executorService.shutdown();
		
	}
	
}
