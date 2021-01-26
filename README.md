# CircularProgressbar in Android

![circularprogressbar](https://user-images.githubusercontent.com/22986571/29291388-0aa337e0-8161-11e7-945c-1394dc9bcc1c.jpg)

CircularProgressbar project let create circular progressbar in android in simplest way.

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-13%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=13)

USAGE
-----
To make a circular Progressbar add CircularProgressbar in your layout XML and add CircularProgressbar library in your project or you can also grab it through Gradle:

Gradle
------
```
dependencies {
    ...
    implementation 'com.jackandphantom.android:circularprogressbar:1.2.0'
}
```
NOTE
------
For CircularImageview, here is library created by me :- https://github.com/sparrow007/CircularImageview

XML
-----

```xml
<!-- <a> circular progressbar xml</a> -->
<com.jackandphantom.circularprogressbar.CircleProgressbar
    android:layout_width="250dp"
    android:layout_height="250dp"
    app:cpb_progress="50"
    app:cpb_roundedCorner="true"
    app:cpb_foregroundProgressWidth="15"
    app:cpb_foregroundProgressColor="#1864c2"
    app:cpb_touchEnabled="true"/>
```
You may use the following properties in your XML to change your CircularProgressbar.

#####Properties:

To add touchEvent into your ciruclar Progressbar you have to add property app:cpb_touchEnabled="true"

/*  circular progressbar xml */
*   app:cpb_roundedCorner            (boolean)  ->  default false
*   app:cpb_foregroundProgressWidth  (Integer)  ->  default 10
*   app:cpb_backgroundProgressWidth  (Integer)  ->  default 10
*   app:cpb_backgroundProgressColor  (Color)    ->  default Color.GRAY
*   app:cpb_foregroundProgressColor  (Color)    ->  default Color.BLACK
*   app:cpb_progress                 (Float)    ->  default 0
*   app:cpb_touchEnabled             (boolean)  ->  default false
*   app:cpb_clockwise                (boolean)  ->  default false

JAVA
-----

```java
CircleProgressbar circleProgressbar = (CircleProgressbar)findViewById(R.id.yourCircularProgressbar);
circleProgressbar.setForegroundProgressColor(Color.RED);
circleProgressbar.setBackgroundColor(Color.GREEN);
circleProgressbar.setBackgroundProgressWidth(15);
circleProgressbar.setForegroundProgressWidth(20);
circleProgressbar.enabledTouch(true);
circleProgressbar.setRoundedCorner(true);
circleProgressbar.setClockwise(true);
int animationDuration = 2500; // 2500ms = 2,5s
circleProgressbar.setProgressWithAnimation(65, animationDuration); // Default duration = 1500ms
```
* There is progress listener interface you can implement as par your requierments and there are also getter for all the above methods.

## How to contribute?

1. Fork the repository 
2. Do the desired changes (add/delete/modify)
3. Make a pull request

## When to contribute?

1. Fix open bugs.
2. Add new issue.


LICENCE
-----

 Copyright 2017 Ankit kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 Thanks to stackoverflow and Raggav

