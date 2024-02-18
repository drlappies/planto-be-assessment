### Requirements

You are required to create a csv editor web-app (similar to a spreadsheet).

Assumptions you can make:
* CSVs are restricted to a strict format with any 5 columns of your choice.

There should be two main screens:
1. A screen which shows a list of uploaded csvs (and an upload button allowing upload of files)
2. A screen which allows editing of a given uploaded csv in a spreadsheet-like editor which also has a 'save' button

The user flow should be:
1. upon opening the web-app, the user should be shown screen 1
2. if they want to upload a new csv, they can upload it on screen 1. 
3. once a file is uploaded, it should keep the original file name and show in the list of uploaded files on screen 1.
4. each item in the file list should have an edit button on it
5. clicking an edit button should open screen 2
6. screen 2 should allow users to make changes on a spreadsheet like interface 
7. changes should save with a save button
8. once saved, the user goes back to screen 1
