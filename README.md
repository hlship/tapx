TapX: Tapestry 5 Extensions by Howard Lewis Ship
====

The TapX modules are a set of extensions to the [Apache Tapestry Web Framework](http://tapestry.apache.org]) that provide new capabilities that are not yet available in the main framework.

In some cases, a module exists here for licensing reasons, as it may integrate with an existing library that is not compatible with the Apache Software License.

In other cases, a module represents new functionality that is being refined before moved into Tapestry proper; alternately, it may represent code that is not of sufficient general interest to be part of framework, proper.

Currently, snapshots of the modules are available via the Maven repository at [http://howardlewisship.com/snapshot-repository](http://howardlewisship.com/snapshot-repository).

Improved documentation is coming (we're in the middle of a transition from Maven to Gradle as the build and packaging tool).

tapx-core
---------

This module provides the following features:

* "cond:" binding prefix
* Support for TEST_MODE symbol
* Silk 1.3 icon set from [FamFamFam](http://www.famfamfam.com/lab/icons/silk/)
* Components:
  * SetEditor
  * Dynamic
  * Tree
  * MultipleSelect
* Mixins:
  * Confirm
  
tapx-datefield
--------------

Add a replacement for the standard Tapestry DateField component, based on [Dynarch JSCalendar Widget](http://www.dynarch.com/projects/calendar/old)  (licensed under LGPL)

The new DateField includes options to edit the time (not just the date) and is time zone aware. The TimeZoneIdentifier component uses client-side JavaScript to determine the user's time zone
base on Geolocation data (if allowed by the client).

* Components:
  * DateField
  * TimeZoneIdentifier

tapx-heroku
-----------

A small wrapper that allows Tapestry applications to be deployed onto the Heroku cloud computing service.

The documentation is currently just a posting on [James Ward's blog](http://www.jamesward.com/2012/02/08/deploy-containerless-tapestry-apps-on-heroku).

tapx-plainmessage
-----------------

This is a very simple extension to Tapestry.  It adds a new binding prefix, "plain:".

Plain works just like "message:", except that any HTML elements are scrubbed, and XML
entities are replaced with the corresponding characters.

tapx-templating
---------------

This library is an extension to Tapestry that allows it to be used in a completely different way: as a templating engine used to generate offline content.

tapx-yui
--------

Bundles [YUI 2.8.0r4](http://developer.yahoo.com/yui/2/).

* @ImportYUI annotation
* Components:
  * RichTextEditor
  
  

