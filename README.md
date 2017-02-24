# Sakai and Codefresh.io

This project let anyone run a Sakai 11.x or higher server for testing purposes.
You have to follow a few steps and wait until Codefresh do the work and show you a URL to connect to your test server.

# Configuration

	- Create a codefresh account (if you don't have one already).
	- Add Service
	- Choose this repo as URL and choose use existing codefresh.yml file.
	- Configure launching settings (see next section).
	- Build your server.

# Launch Settings

	- Set variables to configure the test server you want to run:
	- REPO_OWNER=sakaiproject,myfork,<any_github_account>
	- REPO_NAME=sakai
	- REPO_REVISION=branch,hash_value,...
	- SAKAI_DATABASE=mariadb,mysql,oracle //Choose the database you want to use in your environment
	- BINARY_RELEASE=11.0,11.1,11.2,... // Choose a release and don't build the code just test binary release (fast)
	- MYSQL_VERSION=5.6.27,..
	- ORACLE_VERSION=oracle-xe-11g,oracle-12c
	
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
	- SAKAI_DATABASE=mysql
	
Launch the build and wait.
