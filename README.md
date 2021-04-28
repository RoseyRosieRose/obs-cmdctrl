# OBS-Cmdctrl

## Introduction

A simple application to bind midi events to OBS actions, via the obs-websocket api.

Built upon Spring Boot and [websocket-obs-java](https://github.com/Twasi/websocket-obs-java)

Utilising [cliche](https://github.com/budhash/cliche) for the user interface.

## Why?

### What for?
* Need a way to control my dedicated stream computer without swapping between them.
 * Remote desktop puts strain upon target computer, it's old and rickety, streaming takes a lot out of it.
 * Web interfaces that exist for OBS-websocket are all kitchy proof of concepts / customisable.
   * I've long maintained that Web Applications are an oxymoron. (They are neither of the web or full applications).
 * I have an AKAI MPK Mini Midi controller lying around. 
 
### What would sastisifice and get me 80% of what I need?

Simple console user interface, load it up, select a midi device and start listening.
Create and try out new bindings on the fly, customising actions etc.
Need to be able to list bindings and what they currently do.
Later put those bindings into YML so they can be used again at a later date.

### Why this way?
* Java may be laughed at by many, but it's a highlevel language that's fairly easy to prototype with.
* Spring boot abstracts away all of the configuration concerns in a way I'm familiar with.
* Good cross platform support unless you try to do anything too fancy.
* Midi support is part of the Standard Library and is solid.

## Requirements:

* OBS-Studio with OBS-Websocket plugin install and configured.
* JRE 8+
* Any generic midi device

