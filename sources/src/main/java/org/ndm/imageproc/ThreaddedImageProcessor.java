package org.ndm.imageproc;

import java.util.concurrent.ExecutorService;

public class ThreaddedImageProcessor implements ImageProcessor{

	private ExecutorService executorService = null;
	
	
	private void init(Integer numThreads){
		
	}
	
	@Override
	public void process(ImageProcessingRecipe recipe) {
		
	}

	@Override
	public void processAndStore(ImageProcessingRecipe recipe, String outputFileName) {
		
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
}
