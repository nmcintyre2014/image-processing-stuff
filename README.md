# image-processing-stuff - A simple demonstration of pipelined, deferred-execution image processing.

## What is pipelined, deferred-execution image processing (PDIP)?
Pipelined, deferred-execution image processing is an architectural pattern for developing complex image processing software in a simple, efficient manner.  It's not a new pattern.  It's been around for decades.  Despite it's usefulness, I believe that it's under-utilized in industry and academia.  Recent technology advancements (in particular, the advent of "the cloud") has made this nice, old architectural pattern very relevant again.  This code repository attempts to highlight the usefulness of PDIP by using it to do something that is both complex enough to be non-trivial and somewhat useful in the real-world - orthorectify images from low-flying aircraft (drones).

## What is PDIP good for?
PDIP is good for designing elegant, simple image processing systems without compromising flexibility or performance.  It was originally concieved of as a way to overcome inherent I/O bottlenecks at the CPU for a wide variety of image processing problems.  It did that well and that's still a major benefit of the pattern.  In the modern era of computing, PDIP has proven to bring new types of benefits.  Namely, it allows the developer to separate image processing concerns from compute scheduling concerns.  This makes it easy to develop, for example, mutli-threadded or distributed image processing programs that are both simple and performant.

## Who uses PDIP?
PDIP is used in the geospatial industry to build high-performance image servers.  Some well-known digital photography software developed at a company whose name rhymes with "Nairobi" has been using PDIP to dominate the digital photo processing space for many years.  A few commercial and open-source PDIP libraries exists.  Some of them are:
  * [Java Advanced Imagaing](http://www.oracle.com/technetwork/articles/javaee/jai-142803.html) (JAI - the library our application uses)
  * [Intel IPP DMIP](https://software.intel.com/en-us/forums/intel-integrated-performance-primitives/topic/294955) 
  * [Halide](http://halide-lang.org/) - check out some of the great publications from this relatively new project that explain some of the benefits of PDIP-style processing.

## How to use this software

### Dependencies
  * A Java SDK to build the software or JRE to run it.  I used Java 1.8, but the code should build on older java platforms.
  * Maven 3+ to build the software

### Building the Software
Build and run unit tests (from the 'sources' dirctory - where the pom.xml file resides):
```
mvn clean install
```
Build, run unit tests, generate code coverage report, generate javadocs:
```
mvn clean install cobertura:cobertura javadoc:javadoc
```

### Running the software
To make it relatively easy to run, the software builds into a runnable uber-jar.  This uber-jar (and a Java JRE) should be all one needs to run the software.  The steps for running the software are:
  1 Get the uber-jar by building it (it'll end up in a directory called 'target') or just get a pre-built one from the 'builds' directory.  There are no platform-specific libraries, so the application should run on linux, mac, or windows.
  2 Run the following command:
```
TBD
```
