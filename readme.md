# Readme

Kαλημέρα !

This is the Java Assignement that I fullfiled about Natural Numbers Interpretation.

## Topic

When interacting with an automated dialogue system, the caller is often required to provide his phone
number. Phone numbers may be uttered in many ways with different digit groupings (e.g. 2106930664 may
be uttered as "210 69 30 6 6 4" or "210 69 664" etc). Furthermore, speech input may cause ambiguities. For
example, if the caller says "twentyfive" this could be transcribed as "25" or "20 5".

The goal of this application is to generate every possibilities of number considering the ambiguities comming from natural language.

## Overview

My idea for this problem was to create and store every possibilities of ambiguities in an ArrayList of ArrayList of String. All the possibilities comming from the same ambiguities had to be stored at the same index.

From here, I could generate every phone number by browsing the arrays.

Some edge case like tenth are handled. Indeed, there is no ambiguities in 10, 11, 12 .. 19


## Installation

You can launch the project from your favorite IDE when the POM file is detected.

## Usage

You can type the number you want to test in command line. The prompt will ask it to you.

## Tests

Tests are available, you can launch them with :

```mvn test -e```
