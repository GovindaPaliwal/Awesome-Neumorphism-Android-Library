# Awesome Neumorphism - Android Library

[ ![Download](https://api.bintray.com/packages/gpfreetech/Awesome-neumorphism/Awesome-neumorphism/images/download.svg?version=1.0.1) ](https://bintray.com/gpfreetech/Awesome-neumorphism/Awesome-neumorphism/1.0.1/link)
[ ![Bintray](https://img.shields.io/badge/Bintray-v1.1-red) ](https://bintray.com/gpfreetech/../1.0.1/link)
![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)

![Github Followers](https://img.shields.io/github/followers/govindapaliwal?label=Follow&style=social)
![GitHub stars](https://img.shields.io/github/stars/govindapaliwal/Awesome-Neumorphism-Android-Library?style=social)
![GitHub forks](https://img.shields.io/github/forks/govindapaliwal/Awesome-Neumorphism-Android-Library?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/govindapaliwal/Awesome-Neumorphism-Android-Library?style=social)

Hello Guys, I have created and publish native android library to easily convert android views to Neumorphism UI pattern view in Android app without create any custom drawable.

***What is Neumorphism design ?***

The neumorphic effect is a combination of the current famous flat UI and the old skeuomorphic principles! The components have a dark box-shadow on the bottom and a light box-shadow on top; the combination of both creates the effect of the elements pushing themselves through your display.

Neumorphism is suitable for Simple / minimum page UI / single page UI apps like calculator, music app, weather app etc
Neumorphism is very complicated for a large app but this is my thinking. I am not sure about other’s.

***So, About this Library***

Using this library you can convert your basic views to Neumorphic View using small code. Currently i tested this library on only Normal android views like Button, EditText, TextView, ImageView, Slider, Radio Button, Checkbox, Tabs etc.

## Sample Example App Screens
<table style="width:100%">
  <tr>
    <td><img src="screenshot/orange_example.png"/></td>
    <td><img src="screenshot/blue_example.png"/></td>
    <td><img src="screenshot/yellow_example.png"/></td>
  </tr>
</table>

## Implementation
You can clone this repository and import this project in Android Studio.

### Using Gradle
In your `build.gradle` file of app module, add below dependency to import this library

```gradle
    dependencies {
       implementation 'com.gpfreetech:Awesome-neumorphism:1.0.1'
    }
```

### In Your Working Activity
#### Initializing `Awesome-Neumorphism` :
See below code
```java
        new Neumorphism(this)
               //set views you want to style in comma seperated format list
               .setViews(btn1,edittext1,checkbox1…...etc)
        	   .parentColor(backgroundColor) // madnetory color in int 
               .controlColor(controlColor) // madnetory color in int
               .sharpEdges(TRUE | FALSE) // optional
               .withCurvedSurface() // optional
               .viewShape(CIRCULAR_SHAPE) // optional
               .clipChildren(child_view) //optional 
               .withRoundedCorners(10 or any)
               .build(); // call build to process

```
#### Other Extra Methods
```java
// To set elevation
.Elevation(INT_VALUE)
// To set  seperat background color for view 
.backgroundColor(colorIntValue);
// To set  seperat background drawable for view
.backgroundDrawable(drawable)
```

**Parameter Details:**

<table>
<tbody>
<tr>
<th>Method</th>
<th><span style="&ldquo;font-weight: bold&rdquo;;">Mandatory</span></th>
<th>Description</th>
</tr>
<tr>
<td>setViews(Views..,..,..)</td>
<td>YES</td>
<td>pass all the views you want to convert to Neumorphism</td>
</tr>
<tr>
<td>parentColor()</td>
<td>YES</td>
<td>pass parent color in int format</td>
</tr>
<tr>
<td>controlColor()</td>
<td>YES</td>
<td>pass control color . this is nothing but a accent color</td>
</tr>
<tr>
<td>sharpEdges(TRUE | FALSE)</td>
<td>NO</td>
<td>pass boolean determains if the view's Neumorphism shadow i.e. sharp = true or soft = false</td>
</tr>
<tr>
<td>withCurvedSurface()</td>
<td>NO</td>
<td>Call if you want to view as a surface seems curved. Makes the view surface seems curved with colour shades</td>
</tr>
<tr>
<td>viewShape(Neumorphism.CIRCULAR_SHAPE))</td>
<td>NO</td>
<td>Call if you want to view as a surface seems curved. Makes the view surface seems curved with colour shades</td>
</tr>
<tr>
<td>clipChildren(child_view)</td>
<td>No</td>
<td> Make the image view child rounded corners, if any. You can pass multiple child's to clip.</td>
</tr>
<tr>
<td>withRoundedCorners(VALUE_IN_INT)</td>
<td>NO</td>
<td>
<p>call with value of corners radius for views</p>
</td>
</tr>
<tr>
<td>build()</td>
<td>YES</td>
<td>
<p>It will build (convert view and render with updated view).</p>
</td>
</tr>
</tbody>
</table>


#####Note : 
Currently test only Normal android views like Button, EditText, TextView, ImageView, ToggleButton, Switch, CheckBox, RadioButton,ImageButton, SeekBar, ProgressBar, TabLayout etc.
 
Suggestions are welcome.

## Contribute
If you have any issues, ideas about Neumorphism UI implementation in native android then just raise issue or fork and we are open for Pull Requests. 
You All Welcome.

Developed and maintained By
------------

* Govinda Paliwal (Gpfreetech) - <gpfreetech@gmail.com> 


License
--------
```
Copyright

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
