# Print help
java -jar builds/image-processing-stuff-1.0.jar

# Process the first image
java -Xmx1024M -jar builds/image-processing-stuff-1.0.jar -if test_data/image_coordinates.csv -id DSC02698.JPG -of /tmp/DSC02698_out.tif

# Process the second image
java -Xmx1024M -jar builds/image-processing-stuff-1.0.jar -if test_data/image_coordinates.csv -id DSC03449.JPG -of /tmp/DSC03449_out.tif

# Process the second image again, this time using only one thread
java -Xmx1024M -jar builds/image-processing-stuff-1.0.jar -if test_data/image_coordinates.csv -id DSC03449.JPG -nt 1 -of /tmp/DSC03449_out.tif
