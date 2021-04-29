# OBS-Cmdctrl

## Introduction

A simple application to bind midi events to [OBS](https://github.com/obsproject/obs-studio) actions, via [obs-websocket](https://github.com/Palakis/obs-websocket).

Built upon Spring Boot and Utilising [cliche](https://github.com/budhash/cliche) for the user interface.

## Why?

### What for?
* Need a way to control my dedicated stream computer without swapping between them.
 * Remote desktop puts strain upon target computer, it's old and rickety, streaming takes a lot out of it.
 * Web interfaces that exist for OBS-websocket are all kitchy proof of concepts / customisable.
   * I've long maintained that Web Applications are an oxymoron.
   * They are neither of the web or full applications
   * Any sufficiently large Electron App should have been written in C#
 * I have an AKAI MPK Mini Midi controller lying around. 
 * [OBS-Midi](https://obsproject.com/forum/resources/obs-midi.1023/) is a nice project, but is written in C++ and I've had crashes.
   * Using OBS-Websocket gives us a somewhat stable ABI to attack.
   * Using OBS-Websocket is a network protocol, giving us all the advantages that provides.
 
### What would sastisifice and get me 80% of what I need?

* Simple console user interface, load it up, select a midi device and start listening.
* Create and try out new bindings on the fly, customising actions etc.
* Need to be able to list bindings and what they currently do.
* Later put those bindings into YML so they can be used again at a later date.

### Why this way?
* Java may be laughed at by many, but it's a highlevel language that's fairly easy to prototype with.
* Spring boot abstracts away all of the configuration concerns in a way I'm familiar with.
* Good cross platform support unless you try to do anything too fancy.
* Midi support is part of the Standard Library and is solid.

## Requirements:

* OBS-Studio with OBS-Websocket plugin install and configured.
* JRE 8+
* Any generic midi device, doing wonky keyboard bindings is not a great idea for a streaming environment.
  * This is why the elagato streamdeck is a thing,
  * Nice polished streamlined package of hardware and software dedicated to macroing.

## Project Notes

* OBS-Websocket java libs that are out there are a PITA to use and suck.
  * Use shadowing, high java version requirements, not in any central repo, godawful design etc.
  * So jury rigging it instead, protocol is fairly simple (if ugly), off the shelf websocket client lib is all we need.
  * Maybe extract and cleanup my implementation as a library somewhere down the road?
* This project uses Google java code format 1.6, because I like autoformatters, you should too.

## Progress

* Connecting to OBS ✔️
* Authentication Implemented. ✔️
