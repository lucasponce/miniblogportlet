miniblogportlet
===============

A minimal Blog java portlet.

Copyright (C) 2012 	Lucas Ponce 

Objectives
----------

miniblogportlet is a set of java portlets (based on JSR 168) to offer minimal functionalities of blogging for java portals.

miniblogportlet supports:

- multiple blogs,
- custom style based on CSS,
- internal repository,
- tagging,
- comments,
- in-line editing,
- clustering cappabilities

Portlets description:

	- MinimalBlog:		Portlet to add new blog to your portal
	- ManagementBlog:	Portlet to manage list of blogs of repository

Requeriments
------------

miniblogportlet uses standard java portlets, so it can be migrated easily to any standard PortletContainer based on JSR 168 or JSR 286 standards.

miniblogportlet uses JBoss Cache for internal repository with clustering cappabilities.

miniblogportlet has been tested with JBoss Enterprise Portal Platform 5.2 based on GateIn with "all" type instance.

You can get JBoss EPP 5.2 from here:	

	https://access.redhat.com/downloads/evals

Installation
------------

Steps:

	- [1]	Update build.properties with the path of your jboss-epp-5.2 home and with your "all" based instance name.
	- [2]	ant install to compile and deploy minibloglib.jar into $INSTANCE/lib folder and miniblogportlet.war into $INSTANCE/deploy folder.
	- [3]	Start your instance
	- [4]	Take a look into minitutorial about how to create new portal pages with MinimalBlog portlets and start creating blogs.

Configuration
-------------

miniblogportlet uses JBoss Cache as repository, and it's configured into the following path minicmsportlet.war/WEB-INF/miniblog-jboss-beans.xml.

JBoss Cache is configured to use a local folder as cache loader, this folder can be configured with a java system property 

	-Dminiblog.data.dir=<path to your folder repository>

miniblogportlet is a really simple piece of code, you can easily adapt it to your needs.

JBoss Cache is not mandatory, you can rewrite your own repository just adding an implementation of the following interfaces:

	- blog.services.BlogAPI
	- resources.services.ResourcesAPI

You can take:

	- blog.services.CacheBlogImpl and 
	- resources.services.CacheResourcesImpl 

as examples of implementations but the aim of this design is to add different implementations in a simple way.

Tutorial
--------

- 001_add_blog_as_a_portlet.png

Once you have deployed your portlet, you can add it to a portal page.
You will have three portlet preferences:

	- users:	Users that can create, modify or delete entries and delete comments (comments are free for every users).
	- blog:		name of the blog, if the blog doesn't exist it will be created.
	- entries_page:	Number of entries por pagination.

	- You can define multiple blogs inside same portal.

- 002_default_blog_css.png

By default, miniblogportlet has a default style.

- 003_custom_blog_css.png
- 004_custom_blog_css.png

	- Style of the blog can be modified via CSS.
	- Accesing to "Edit Blog" link you can access to an admin blog page.
	- You can add resources (.css files or images to the internal miniblogportlet repository).
	- You can reference to this resources into Blog configuration and inside .css pages.
	- Take a look to the examples folder to study how to modify the .css style (template is really simple).

- 005_create_new_entry.png

	- Create new entry with in-line editor.
	- Only users defined in portlet preferences can add new entries.


- 006_add_comments.png

	- Anonymous users can add comments to the blogs.
	- Admin user can remove comments.

- 007_tagging.png

	- Tags are supported in miniblogportlet.
	- You can add several tags for an entry.
	- All entries can be grouped by tags.

- 008_adding_internal_resources.png
- 008_internal_pictures.png

	- You can edit an entry an add internal resources (images or whatever).
	- You can refer to the internal resource with the "repo:" suffix.
	- "repo:" suffix will be translated to the internal URL: /miniblogportlet/resource?blog=<blog name>&entry=<entry name>&key=<resource name/key>

- 009_removing_comments_and_entries.png

	- Example about how to delete comments and entries.

- 010_paging

	- Example about how to paging entries based entries_page preference

- 011_simple_blog_management.png

	- ManagementBlog portlet showing how to list all blogs of portal with delete cappabilities.

- 012_easy_style_customization.png

	- Example of customization of style in a blog.

Feedback
--------

Please, feel free to give feedback for future modifications.

https://github.com/lucasponce/miniblogportlet

ponce.ballesteros@gmail.com

Thanks !


