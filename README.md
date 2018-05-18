# In-Store Analytics

Click-throughs, bounce rates, segmentation and conversion funnels are common and essential metrics for any online store. Brands which do not employ these to enhance the experience will miss out on targeted content, display optimization and personalized recommendations which account for over 20% of an online-stores revenue.

We take the latest artificial intelligence breakthroughs in the field of vision-based Human Computer Interaction (HCI) to change the game. AI algorithms detect your visitors demographics - such as age, gender, which can be leveraged for marketing compaign segmentations. Capturing the emotional state of your visitor is a new way to measure customer satisfaction in a digital ear. 

Learn from consumer behavior to improve conversion rates and transaction size. Measure the entire-funnel from walk-by through to completing a sale. Automatically exclude store staff from traffic counting and other KPI metrics. Compare your performance against your industry on a local, regional or national level.


With In-Store Analytics, B1 Retailers can leverage the power of digital footprints to apply the same tools as their online counterparts. 

<img src="https://raw.githubusercontent.com/martinambition/FaceRecognitionAPI/master/inshop.jpg" width="100%">


![image](https://raw.githubusercontent.com/martinambition/FaceRecognitionAPI/master/report1.png)

![image](https://raw.githubusercontent.com/martinambition/FaceRecognitionAPI/master/report2.png)



# Installation
You can install In-Store Analytics in either of the following ways:

* if you want to use the standard package, install from the release build
* if you want to customise the analytic functions, install by using the source code 


***Currently, In-Sotre Analytics is available on the Windows platform only.***


---
## Install from the release build:

### Hardware
* USB Camera, (e.g. Logitech C922)

### Prerequisit
You have enabled the followings on your computer:

* Java runtime environment
* [SQL Server](https://www.microsoft.com/en-in/download/details.aspx?id=30438/)

### Procedure
1. Download the release [build](https://github.com/smbinnovationlab/InStoreAnalytics/releases/download/V0.1/InStoreAnalytcis-builds.zip)
2. Extract the zip file “InStoreAnalytcis-builds.zip”
3. In SQL Server, create a database
    1. Name the database "**ShopAnalytics**"
    2. Execute "DB/**DBCreate.sql**"

### Running In-Store Analytics
1. Navigate to unzip folder
2. Run "InitData.bat"
3. Run "CameraServer.exe" 
4. Run "AnalyticsServer.bat", and enter the account name and password of the created database
5. In any browser, go to http://IP:8080/client/index.html

----

## Install from the source code:
In-Store Analytics contains three parts: the database, the camera server, and the analytics server.

### Prerequisit
You have enabled the followings on your computer:

  * [SQL Server](https://www.microsoft.com/en-in/download/details.aspx?id=30438/)
  * [Anaconda](https://www.anaconda.com/download/) 
  * JDK 1.8
  * [Gradle](https://gradle.org/install/#manually)

### Procedure

1. Clone or dowload source code from the repo
2. In SQL Server, create a database
    1. Name the database "**ShopAnalytics**" 
    2. Execute "DB/**DBCreate.sql**"
3. Install the camera server, using python
    1. From the Start menu, open your Anaconda Prompt
    2. Change the directory to the "camera-server-python" folder
    3. Execute the following command:  
        ```console
            conda env create -f environment.yml -n face
        ```
    4. Download the face recognization models from [here](https://drive.google.com/open?id=1NRhwIGPhyhR9uqQb8EmZigEwOoirTO_8), and extract to "camera-server-python\face_process\models"
4. Install the analytics server, using java
    1. Change the directory to the "analytics-server-java" folder
    2. In file "application.properties", fill the connection strings of the database  
    3. Execute the following command:  
        ```console
            gralde build
        ```

### Running In-Store Analytics
1. Run "InitData.bat"
2. Execute the following command to start the python server:
    ```console
        python server.py
    ```
3. Execute the following command with your own username and password:
    ```console
        java -jar ServerCenter-0.0.1.jar
    ```
4. In any browser, go to http://IP:8080/client/index.html

## Tips
- In-Store Analytics is a loosely coupled solution. Currently we provide the mock up data, but you can integrate this solution with your ERP system (e.g. SAP Business One, SAP ByDesign etc.)
- If you want to deploy the analytics server (pure java application) in SAP Cloud Platform. You could follow this [link](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/e5dfbc6cbb5710149279f67fb43d4e5d.html)

## License
In-Store Analytics is released under the terms of the MIT license. For more information, see LICENSE or visit https://opensource.org/licenses/MIT.

