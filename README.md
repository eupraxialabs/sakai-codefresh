# Sakai and Codefresh

> Let's try any sakai branch easy
 
This project let anyone run a Sakai 11.x or higher server for testing purposes.
You have to follow a few steps and wait until [Codefresh.io](http://codefresh.io/) do the work and show you a URL to connect to your test server.
_Codefresh_ will create a _Sakai docker image_ ready to run with any supported database, once you have the image created you can run it everytime you need, without having to build it again. Maybe you want to rebuild the image for SNAPSHOT branches, is up to you.

## Contents

- [Sakai and Codefresh.io](#sakai-and-codefresh)
	- [Configuration](#configuration)
	- [Build Settings](#build-settings)
	- [Build a binary release](#build-a-binary-release)
	- [Oracle Configuration](#oracle-configuration)
	- [Some things you may want](#some-things-you-may-want)
		- [I Want to test some Sakai PR](#i-want-to-test-some-sakai-pr)
		- [I Want to launch a previous image](#i-want-to-launch-a-previous-image)
		- [I Want to test some sakai property](#i-want-to-test-some-sakai-property)
	- [More tips and tricks](#more-tips-and-tricks)

- - -

## Configuration

Create a codefresh.io account (if you don't have one already), and follow this steps to get your own test sakai server up and running.

* Configuration steps:
	* _Add new service_ - Add a new service in codefresh. 
	* _Use repo url_ - Choose this repo as URL.
	* _Use ./codefresh.yml_ - Select the codefresh file located in the repo.
	* _Configure build settings_ - See the next section to get more information [here](#build-settings).
	* _Build/launch_ - Now you can build your Sakai image and test it in some URL.

## Build Settings

You can build many different sakai images ready to run at any moment. You just have to set some variables in *launch settings* option:

* Settings:
	* REPO_OWNER=sakaiproject,myfork,... (with sakai source code :D)
	* REPO_NAME=sakai
	* REPO_REVISION=branch,hash_value,...
	* TOMCAT_IMAGE=tomcat:8.0.41-jre8,... // Choose tomcat docker image you want to use
	* SAKAI_DB_DRIVER=mariadb,mysql,oracle,all //Choose the database driver you want to add in the image (all will let you use the image with any database later)
	* BINARY_RELEASE=11.3,... // Choose a release and don't build the code just test binary releases (fast)
	* DB_IMAGE=mysql,mariadb,sakaiproject/oracle // Choose the database docker image
	* DB_VERSION=5.6.27,5.5.54,oracle-xe-11g,oracle-12c
	* SAKAI_TAG=my_tag_name // Use this to tag your new sakai image, master, v11.3, PRxxx, ...

## Build a binary release

The first thing you can do is trying a Sakai binary release, without building from code, just downloading from [Sakai](https://www.sakaiproject.org/download-sakai)

* Steps to build Sakai 11.3 for any database:
	* REPO_OWNER=sakaiproject
	* REPO_NAME=sakai
	* SAKAI_TAG=v11.3
	* TOMCAT_IMAGE=tomcat:8.0.41-jre8
	* BINARY_RELEASE=11.3
	* SKIP_LAUNCH=true
	* SAKAI_DB_DRIVER=all
		* Use _mysql_ to avoid add oracle credentials.
		* MASTER_PASSWORD=...
		* ORACLE_USER=...
		* ORACLE_PASS=...

Build the image and test Sakai 11.3.

## Oracle Configuration

Codefresh persist your main volume, so your maven repo will be shared between builds, and also your server deployment.
	
For oracle database you need the oracle jdbc driver, and that requires and account, so you need to go to https://maven.oracle.com to get and account and accept the Oracle terms and conditions to download it.

* Oracle settings:
	* ORACLE_USER=your_oracle_user - Your oracle account user
	* ORACLE_PASS=your_oracle_pass - Your oracle account password
	* MASTER_PASSWORD=some_pass - Any password to encrypt maven settings

* Once you download the driver, it persists in codefresh volume, so you can skip this step:
	* Delete _MASTER_PASSWORD_ variable.
* Also for oracle database you have to build the database image, once you have the image in your local docker registry you can skip this step:
	* Add variable `SKIP_ORACLE_BUILD=true`

## Some things you may want

This repo let you do multiple things without having to do so many steps or need to now about Sakai technology. Also it could be useful to test Sakai with different databases or tomcat versions in minutes. 

### I Want to test some Sakai PR

You can easily test any Sakai PR following a few steps, supposing you want to test [#2368](https://github.com/sakaiproject/sakai/pull/2368)

* Pull request settings:
	* REPO_OWNER=master-bob
	* REPO_NAME=sakai
	* REPO_REVISION=SAK-31068_ForumsImportAsDraft
	* SAKAI_TAG=PR_2368 // You could use any name you want
	
Now you can build the image and test it.

### I Want to launch a previous image

You can run again any build you already create, for example, if you build a Sakai 11.3 from binary, and you want to run it again, just launch the image, you don't have to rebuild it is already in your local docker registry.

* Launch any image:
	* Check availability - The image should be available in _Images_ at codefresh.
	* Create a composition - Go to compositions and add a new composition.
	* Use repo url - Choose this repo as URL.
	* Use ./docker-compose.yml - Use the docker-compose.yml file located in the repo.
	* Composition variables - Add the composition variables you need.
		* DATABASE_IMAGE=mysql:5.6.27
		* DATABASE_TYPE=mysql
		* SAKAI_IMAGE=sakaiproject/sakai:11.3
	* Launch - Click launch to run Sakai. 

### I Want to test some sakai property

Sakai images are build with all its default properties. If you want to change the default value for some property you don't need to rebuild the image compiling the code. You can build the image from the original one just adding your property changes.
For example you can test some property in a previous 11.3 sakai build.

* Testing experimental properties:
	- EXPERIMENTAL_PROPS=some_url - Any URL with a set of properties, like [this](https://raw.githubusercontent.com/sakaiproject/nightly-config/master/experimental.properties)
	- PROPERTIES_FILE=local.properties,sakai.properties,... - Any properties file you want to overwrite.
	- SAKAI_BASE_TAG=sakaiproject/sakai:11.3 - The existing image you want to try with this properties. 
	- SAKAI_TAG=v11.3_experimental - You could use any name to tag new image. 
	- Build - Build the image

## More tips and tricks

* Some tricks to know:
	* You can build images and not launching immediately
		* SKIP_LAUNCH=true
	* You can create an image with some additional properties using some etherpad.
		* Create and etherpad - You could use the beta [here](https://beta.etherpad.org/)
		* Write the properties - Fill the etherpad with your properties
		* SAKAI_BASE_TAG=sakaiproject/sakai:11.3
		* EXPERIMENTAL_PROPS=https://beta.etherpad.org/p/your-etherpad-name/export/txt
		* PROPERTIES_FILE=local.properties - To add properties in this file
		* SAKAI_TAG=v11.3_experimental - Any name you want to use to tag the new image
		* SKIP_LAUNCH=true - Do not run the image, just build it.
	* You can create multiple pipelines to build different Sakai images.
		* Create pipeline and set environment variables
