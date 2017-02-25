# Sakai and Codefresh.io

This project let anyone run a Sakai 11.x or higher server for testing purposes.
You have to follow a few steps and wait until Codefresh do the work and show you a URL to connect to your test server.

# Configuration

Create a codefresh.io account (if you don't have one already), and follow this steps to get your own test sakai server up and running.

	- Add Service
	- Choose this repo as URL and choose use existing `./codefresh.yml` file.
	- Configure build settings (see next section).
	- Build/launch your server image.

# Build Settings

You can build many different sakai images ready to run at any moment. You just have to set some variables in `launch settings` option:

	- REPO_OWNER=sakaiproject,myfork,<any_github_account> (with sakai source code :D)
	- REPO_NAME=sakai
	- REPO_REVISION=branch,hash_value,...
	- TOMCAT_IMAGE=tomcat:8.0.41-jre8,... // Choose tomcat version you want to use
	- SAKAI_DB_DRIVER=mariadb,mysql,oracle //Choose the database driver you want to use in your environment
	- BINARY_RELEASE=11.0,11.1,11.2,... // Choose a release and don't build the code just test binary releases (fast)
	- DB_IMAGE=mysql,mariadb,sakaiproject/oracle // Choose the database docker image
	- DB_VERSION=5.6.27,5.5.54,oracle-xe-11g,oracle-12c
	- SAKAI_TAG=<anystring> // Use this to tag your new sakai image, master, 11.3, PRxxx, ...
	
# Advanced Configuration

Codefresh persist your main volume, so your maven repo will be shared between builds, and also your server deployment.
In some cases you may want to skip some steps of the build pipeline.

	- PIPELINE_SKIP=build,all
	
For oracle database you need the oracle jdbc driver, and that requires and account, so you need to go to https://maven.oracle.com to get and account and accept the Oracle terms and conditions to download it.

	- ORACLE_USER=<your_oracle_user>
	- ORACLE_PASS=<your_oracle_pass>
	- MASTER_PASSWORD=<any_maven_master_password_you_want>

# I Want to test a Sakai PR

Look at the PR for the fork and branch, something like somefork/sakai:SAK-XXXX.

	- REPO_OWNER=somefork
	- REPO_REVISION=SAK-XXXX
	- SAKAI_TAG=PRXXXX
	
Launch the build and wait.

# I Want to launch a previous image

You can run again any build you already create, for example, if you build a Sakai 11.3 from binary, and you want to run it again, just launch the image, you don't have to rebuild it is just a tag.

	- Create a composition in codefresh.io
	- Choose from this repo docker-compose.yml file
	- Set the variables in the composition to match your goal.
	- Launch.

# Want to test some special sakai property

Sakai images are build with all its default properties. If you want to run some property you must to create a new image, but don't worry you could create one based on a previous build.
For example you can test some property in a previous 11.3 sakai build.

	- Set EXPERIMENTAL_PROPS variable to the URL with the property file you want to use.
	- Set SAKAI_BASE_TAG to the image tag you want to use as base image to add the new properties. 
	- Set SAKAI_TAG to the tag you want to use to name the new image. 
	- Build.
	- Then launch the image created.

