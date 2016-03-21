# image-processing-stuff - A simple demonstration of pipelined, deferred-execution image processing.

## What is pipelined, deferred-execution image processing (PDIP)?
Pipelined, deferred-execution image processing is an architectural pattern for developing complex image processing software in a simple, efficient manner.  It's not a new pattern.  It's been around for decades.  Despite it's usefulness, I believe that it's under-utilized in industry and academia.  Recent technology advancements (in particular, the advent of "the cloud") has made this nice, old architectural pattern very relevant again.  This code repository attempts to highlight the usefulness of PDIP by using it to do something that is both complex enough to be non-trivial and somewhat useful in the real-world - orthorectify images from low-flying aircraft (drones).

## What is PDIP good for?
PDIP is good for designing elegant, simple image processing systems without compromising flexibility or performance.  It was original concieved of as a way to overcome inherant I/O bottlenecks at the CPU for a wide variety of image processing problems.  It did that well and that's still a major benefit of the pattern.  In the modern era of computing, PDIP has proven to bring new types of benefits.  Namely, it allows the developer to separate image processing concerns from compute scheduling concerns.  This makes it easy to develop, for example, mutli-threadded or distributed image processing programs that are both simple and performant.  PDIP allows for near-optmial 

## Who uses PDIP?
PDIP is used in the geospatial industry to build high-performance image servers.  A very well-known photography company has been using PDIP to dominate the digital photo processing space for many years.
