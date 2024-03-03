# MTImageMapView  
## 🏗️👷‍♂️ iOS MTImageMapView Android port 🏡


An image map view to select a complex polygon map out of many.  
Extremely useful for handling touches on, for example, Europe map, or an eye of owl.  

## Screen Shots  

![screenshot](https://raw.githubusercontent.com/stkim1/view.MTImageMapView/main/screenshot.jpg)

## Features  

- Handling multiple maps on an image<sup>1</sup>  
- Batch mapping  
- Delegate to provide selected map index  
- Debug mode to superimpose maps' path on an image  

1. There is no limit but you need to be reasonable. In this example, I put around 50.  

## Support  

- Android API +24  

## Implementation  
1. Use tools like [Gimp](http://www.gimp.org/) and generate a image map.  
2. Copy only coordinate `[x,y]` pairs of the map (e.g. "[123,242],[452,242],[142,322]").  
3. Put the strings in JSON.  
4. Instantiate ImageMapView and implement `MTImageMapTouch` interface.  
5. pass the array to the map view.  


```kotlin
val pString = assets.open("us_states.json")
                .bufferedReader()
                .use { it.readText() }

val moshi = Moshi.Builder()
                .add(PolygonAdapter())
                .build()

val pList: List<MTPolygon> = moshi
                .adapter<List<MTPolygon>>()
                .fromJson(pString) as List<MTPolygon>

val usState : Drawable? = AppCompatResources
                .getDrawable(applicationContext, R.drawable.us_states)

val mapView : MTImageMapView = findViewById(R.id.imageMapView)
mapView.setImageDrawable(usState)
mapView.setTouchedMapReceiver(this)
mapView.setShowPath(true)
mapView.setPolygons(pList)
```  

### LIMITS  

- Delegate only receives the index of a map  
- Coordinates must be provided in pairs  
- At least 3 pairs of coordinates must be presented  
- No "rect", "circle" type map is supported. "Polygon" only at this time being  

## Credits  
- US states image and all coordinates are credited to [Illinois Center for Information Technology and Web Accessibility](http://html.cita.illinois.edu/text/map/map-example.php)  

## Version

_VER_ : 0.1  

_UPDATED_ : Mar. 1, 2024