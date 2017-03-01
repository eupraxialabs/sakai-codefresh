# Sakai and Codefresh [![Codefresh build status]( https://g.codefresh.io/api/badges/build?repoOwner=juanjmerono&repoName=sakai-codefresh&branch=master&pipelineName=sakai-from-source&accountName=juanjmerono_github&type=cf-1)]( https://g.codefresh.io/repositories/juanjmerono/sakai-codefresh/builds?filter=trigger:build;branch:master;service:58b20512a6eaef01002599bd~sakai-from-source)

> Let's try any sakai branch easy

This project let anyone run a Sakai 11.x or higher server for testing purposes.
You have to follow a few steps and wait until [Codefresh.io](http://codefresh.io/) do the work and show you a URL to connect to your test server.
_Codefresh_ will create a _Sakai docker image_ ready to run with any supported database, once you have the image created you can run it everytime you need, without having to build it again. Maybe you want to rebuild the image for SNAPSHOT branches, is up to you.

## Contents

- [Sakai and Codefresh.io](#sakai-and-codefresh)
	- [Configuration](#configuration)
	- [Build Settings](#build-settings)
	- [Build a binary release](#build-a-binary-release) (Walkthrough)
	- [Oracle Configuration](#oracle-configuration)
	- [Some things you may want](#some-things-you-may-want)
		- [I Want to test some Sakai PR](#i-want-to-test-some-sakai-pr)
		- [I Want to launch a previous image](#i-want-to-launch-a-previous-image)
		- [I Want to test some sakai property](#i-want-to-test-some-sakai-property)
	- [More tips and tricks](#more-tips-and-tricks)
	- [Complete list of variables](#complete-list-of-variables)

- - -

## Configuration

Create a codefresh.io account (if you don't have one already), and follow this steps to get your own test sakai server up and running.

* Configuration steps:
	* _Add new service_ - Add a new service in codefresh.
	* _Use repo url_ - Choose this repo as URL.
	* _Use ./codefresh.yml_ - Select the codefresh file located in the repo.
	* _Configure build settings_ - See the next section to get more information [here](#build-settings).
	* _Build/launch_ - Now you can build your Sakai image and test it in some URL.

Jump to [Build a binary release](#build-a-binary-release) example for a walkthrough.

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
	* SAKAI_IMAGE_NAME=sakaiproject/sakai // The name for the sakai image (If you want to promote to a public registry use your-registry-user/sakai)
	* SAKAI_TAG=my_tag_name // Use this to tag your new sakai image, master, v11.3, PRxxx, ...

## Build a binary release

The first thing you can do is trying a Sakai binary release, without building from code, just downloading from [Sakai](https://www.sakaiproject.org/download-sakai)

Ok, first you need to create a Service, click on "Add Service"

![add_service](/images/pic000.png)

Select this repo "https://github.com/juanjmerono/sakai-codefresh"

![select_repo](/images/pic001.png)

Codefresh offers three options to build a service, we are going to use the first one "Codefresh.yml"

![build_method](/images/pic002.png)

There is only one "codefresh.yml" file so click on "Next"

![next_yml](/images/pic003.png)

and finally "Create"

![review_yml](/images/pic004.png)

Nice! You just added your first service, click on build to continue.

![service_added](/images/pic005.png)

Codefresh will try to build the service, it will fail... Don't worry we just need to add the
environment variables.

Go to "Launch Settings"

![launch_settings](/images/pic006.png)

and use the following variables (we are using mysql for this example) :
* Build variables
	* REPO_OWNER=sakaiproject
	* REPO_NAME=sakai
	* SAKAI_IMAGE_NAME=sakaiproject/sakai
	* SAKAI_TAG=v11.3
	* TOMCAT_IMAGE=tomcat:8.0.41-jre8
	* BINARY_RELEASE=11.3
	* SKIP_LAUNCH=true
	* SAKAI_DB_DRIVER=mysql
	* DB_IMAGE=mysql
	* DB_VERSION=5.6.27

![launch_settings](/images/pic007.png)

Ok, ready? Click on "Build" to start building the service.

The logs are displayed so you can follow the building process, be patient...

![build_logs](/images/pic008.png)
![build_logs2](/images/pic009.png)

Since we are building a binary release this won't take too long.

![build_finished](/images/pic010.png)

If you go to "Builds" you will see that the service was successfully built.

![build_list](/images/pic011.png)

Now we need to "launch" the service. Ideally if you click on the rocket image of your build it will launch the service but unfortunately this option doesn't seem to work.

Don't worry!! We just need to create a composition. Go to "Compositions" and click on "Add Composition"

![add_composition](/images/pic012.png)

Select a name, for example "Sakai 11.3"

![composition_name](/images/pic013.png)

Choose "File in Repo"

![composition_type](/images/pic014.png)

and paste this repository "https://github.com/juanjmerono/sakai-codefresh"

![composition_repo](/images/pic015.png)

click on "Create"

![composition_yml](/images/pic016.png)
![composition_create](/images/pic017.png)

Now we need to tell the composition which images use for launching the service.
* Composition variables:
	* DATABASE_IMAGE=mysql:5.6.27
	* DATABASE_DRIVER=mysql
	* SAKAI_IMAGE=sakaiproject/sakai:v11.3

![composition_variables](/images/pic018.png)

Save and click on the rocket to launch the service.

A log for the composition process will appear.

![composition_log](/images/pic019.png)

Click on "Environment" , wait for your composition to be ready (it will change to "Running" state)

![env_running](/images/pic020.png)

If you click in "More Information" you will see the logs for Mysql and Tomcat.

![env_information](/images/pic021.png)

Almost there!! Just wait for the tomcat to finish the startup process!

![env_ready](/images/pic022.png)

After that, click on "Open App" on the Sakai image. A new window will pop up with your Sakai 11.3 instance. Congratulations!

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

### I want to test my Branch
Before creating a PR you may want to test your branch (or somebody's branch). Let's say you want to test this branch
https://github.com/juanjmerono/sakai/tree/SAK-32235

* Branch settings:
	* REPO_OWNER=juanjmerono
    * REPO_NAME=sakai
    * REPO_REVISION=SAK-32235
    * TOMCAT_IMAGE=tomcat:8.0.41-jre8
    * SAKAI_IMAGE_NAME=juanjmerono/sakai
    * SAKAI_TAG=SAK-32235
    * Oracle configuration
    	* SAKAI_DB_DRIVER=oracle
        * DB_IMAGE=sakaiproject/oracle
        * DB_VERSION=oracle-xe-11g
        * ORACLE_USER=Your oracle user
        * ORACLE_PASS=Your oracle password
        * MASTER_PASSWORD=Any password to encrypt maven settings
    * MySql configuration
    	* SAKAI_DB_DRIVER=mysql
    	* DB_IMAGE=mysql
		* DB_VERSION=5.6.27

### I Want to test some Sakai PR

You can easily test any Sakai PR following a few steps, supposing you want to test [#2368](https://github.com/sakaiproject/sakai/pull/2368)

* Pull request settings:
	* REPO_OWNER=master-bob
	* REPO_NAME=sakai
	* REPO_REVISION=SAK-31068_ForumsImportAsDraft
	* SAKAI_IMAGE_NAME=master-bob/sakai
	* SAKAI_TAG=PR_2368 // You could use any name you want

Now you can build the image and test it.

### I Want to launch a previous image

You can run again any build you already create, for example, if you build a Sakai 11.3 from binary, and you want to run it again, just launch the image, you don't have to rebuild it is already in your local docker registry.

* Launch any image:
	* Check availability - The image should be available in any public docker registry.
	* Create a composition - Go to compositions and add a new composition.
	* Use repo url - Choose this repo as URL.
	* Use ./docker-compose.yml - Use the docker-compose.yml file located in the repo.
	* Composition variables - Add the composition variables you need.
		* DATABASE_IMAGE=mysql:5.6.27,...
		* DATABASE_DRIVER=[mysql,mariadb,oracle] // One of the supported databases for Sakai
		* SAKAI_IMAGE=sakaiproject/sakai:v11.3
	* Launch - Click launch to run Sakai.
	* If you don't use the variable SAKAI_IMAGE, and directly replace this variable in the composition with an image url, you will be able to launch the composition from the images view in the launch icon of the selected image.

### I Want to test some sakai property

Sakai images are build with all its default properties. If you want to change the default value for some property you don't need to rebuild the image compiling the code. You can build the image from the original one just adding your property changes.
For example you can test some property in a previous 11.3 sakai build.

You need to promote your image first to a public registry, connect codefresh to one public registry following this [steps](https://docs.codefresh.io/docs/docker-registry)

* Testing experimental properties:
	* EXPERIMENTAL_PROPS=some_url - Any URL with a set of properties, like [this](https://raw.githubusercontent.com/sakaiproject/nightly-config/master/experimental.properties)
	* PROPERTIES_FILE=local.properties,sakai.properties,... - Any properties file you want to overwrite.
	* SAKAI_BASE_TAG=sakaiproject/sakai:11.3 - The existing image you want to try with this properties.
	* SAKAI_IMAGE_NAME=sakaiproject/sakai
	* SAKAI_TAG=v11.3_experimental - You could use any name to tag new image.
	* Build - Build the image

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
		* SAKAI_IMAGE_NAME=sakaiproject/sakai
		* SAKAI_TAG=v11.3_experimental - Any name you want to use to tag the new image
		* SKIP_LAUNCH=true - Do not run the image, just build it.
	* You can create multiple pipelines to build different Sakai images.
		* Create pipeline and set environment variables
	* You can run your build images in your PC after promote them to a public registry.
		* Build images and promote them to some public registry
		* Then use the _docker-compose.yml_ file in this repo to run the image:
			* Create a .env file with the values you want to run:
				* SAKAI_IMAGE=mipublic/image
				* DATABASE_IMAGE=mysql:5.6.27
				* DATABASE_DRIVER=mysql
			* Type: `docker-compose up -d`
			* Look at the port created to access to Sakai instance typing: `docker-compose ps`
			* Connect to http://localhost:port/
	* You can change tomcat region or any other tomcat configuration.
		* Import the docker-compose.yml and change current CATALINA_OPTS or JAVA_OPTS.

## Complete list of variables

Here is the complete list of properties you may want to use:

* Build from source:

	* REPO_OWNER: Github repo owner (sakaiproject or any other fork)
	* REPO_NAME: Github repo name (sakai)
	* REPO_REVISION: Branch, tag, hash you want to use

* Build from binary release:

	* BINARY_RELEASE: One existing Sakai binary release version.

* Build from other image:

	* EXPERIMENTAL_PROPS: Url to download the properties you want to use
	* PROPERTIES_FILE: File name to use (sakai.porperties, local.properties, ...)
	* SAKAI_BASE_TAG: Base image to build the new image from

* Oracle settings:

	* MASTER_PASSWORD: Any password to encrypt maven settings
	* ORACLE_USER: Your oracle user
	* ORACLE_PASS: Your oracle password
	* SKIP_ORACLE_BUILD[true,false]: Do not build oracle image

* Common settings:

	* TOMCAT_IMAGE: Tomcat docker image (Look at the [release notes](https://confluence.sakaiproject.org/display/DOC/Release+Documentation))
	* SAKAI_DB_DRIVER[mariadb,mysql,oracle,all]: Database drive to add to your Sakai image
	* SAKAI_IMAGE_NAME: Name of the image created
	* SAKAI_TAG: Tag for the created image

* Launch settings:

	* DB_IMAGE: Database image to use with your Sakai instance (you must have the right driver loaded in the image)
	* DB_VERSION: Version of the database you want to use
	* SKIP_LAUNCH[true,false]: Do not launch the image created
