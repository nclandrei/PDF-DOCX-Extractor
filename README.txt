                    PDF/DOCX Extraction Tool

What is the project
-------------------

The PDF/DOCX Extraction Tool runs as a Spring web service that allows a user to
upload a PDF or DOCX file from their local machine/Dropbox, which will then have
all of the data tables in it extracted into CSVs, and all of the images
extracted as well. These CSVs are numbered according to the order they were
extracted in, e.g. "table1.csv", "table2.csv"..."tableN.csv", similarly for
images, and placed into two folders "csv" and "images", which are put in another
folder that is given the same name as the file that was uploaded, and compressed
into a Zip and given back to the user.


Uploading a file "example.pdf" would return a file "random.zip" with this
structure:

- random.zip
    - csv
      - table1.csv
      - table2.csv
      ...
      - tableN.csv
    - images
      - image1.jpg/png
      - image2.jpg/png
      ...
      - imageM.jpg/png


Documentation
-------------------
There is extensive use of JavDocs throughout the project, namely for every
class, and every method. There is also some general commenting throughout to
explain some difficult parts.


Downloading
-------------------
The full source code for this project can be obtained by checking out the stable
trunk branch through at hoved.dcs.gla.ac.uk/extra/2015/tp3/x/repos/trunk/
(note you will need to be able to access hoved).


Installation
-------------------
After downloading the latest stable release navigate to the project directory,
"WebApp", in the terminal and tun the command:
    mvn clean package install
Which will install all of the dependencies, and run the tests located in the
src/test/ directory. If all of them pass, and all dependencies have been
installed correctly you should be able to see:
    [INFO] BUILD SUCCESS
near the bottom of the output, if so; the installation has been successful.


Running the project
-------------------
To run the project from the terminal all that needs to be done is to navigate
to the root of the project in a terminal and run:
    mvn spring-boot:run
This will start a web service running on port 8080, to access it got to:
    localhost:8080
The service WILL NOT RUN if port 8080 is currently in use.



Thank you for using our application!

Client
-------------------
Crichton Institute - Regional Observatory


Developers
-------------------
Ian Denev
Ivan Kyosev
Andrei-Mihai Nicolae
Richard Pearson
Edvinas Simkus
