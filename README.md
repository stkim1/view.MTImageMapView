# MTImageMapView  
## 🏗️👷‍♂️ 2013 iOS MTImageMapView Android port 🏡

An image view to select a __complex polygon__ (__`"map"`__ henceforth) out of many. 
Extremely useful for handling touches on, for example, Europe map, or an eye of owl.  

## Screen Shots  

![screenshot](https://raw.githubusercontent.com/stkim1/view.MTImageMapView/main/sample/screenshot.jpg)

## Features  

- Handling multiple maps on an image<sup>1</sup>, just like html [\<area\>](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/area) tag  
- Batch mapping  
- Interface to provide selected map id  
- Debug mode to superimpose maps' polygon path on an image (the left in the screenshot)  

1. There is no limit but you need to be reasonable. In this example, I put about 50 polygon maps on the map in the screenshot above.  

## Support  

- Android API from 24 and up due to Java 8  

## Library Support  

- libs.versions.toml  

```toml
[versions]
stkim1ViewMTImageMapView = "0.1"

[libraries]
stkim1-view-mtimagemapview = {group = "io.github.stkim1", name = "view-mtimagemapview", version.ref = "stkim1ViewMTImageMapView"}
```

- build.gradle.kts
```
dependencies {
   implementation(libs.stkim1.view.mtimagemapview)
}
```

## Implementation with JSON  
1. Use tools like [Gimp](http://www.gimp.org/) and generate a image map.  
2. Take only `[x,y]` coordinate pairs of the map (e.g. `[[123,456],[789,123],[456,789]]`).  
3. Write JSON in the following format.  
```json
[
    {
        "id": "Alabama",
        "vertices": [[409,298],[409,298],[411,292],[409,279]]
    },
    {
        "id": {
            "name": "Alaska",
            "code": "AZ",
            "dial": 907
        },
        "vertices": [[58,294],[56,293],[55,292],[54,292],[51,294]]
    },
    {
        "vertices": [[163,213],[153,290],[131,288],[90,266]],
        "description": "An area map without an id"
    }
]
```
4. For `id` field, you can use good-old string, object, or totally eliminate it if you want to. You can mix up with other fields such as `description`. Nonetheless, the __`vertices`__ field has to be there, and it should maintain coordinations in the aforementioned array format.  
5. Instantiate `MTImageMapView` and implement `MTImageMapTouch` interface.  
6. Pass the array to the instance of `MTImageMapView`.  
```kotlin
val jsonMaps = assets.open("us_states.json")
                .bufferedReader()
                .use { it.readText() }

val moshi = Moshi.Builder()
                .add(PolygonAdapter())
                .build()

val mapList: List<MTPolygon> = moshi
                .adapter<List<MTPolygon>>()
                .fromJson(jsonMaps) as List<MTPolygon>

val usState : Drawable? = AppCompatResources
                .getDrawable(applicationContext, R.drawable.us_states)

val mapView : MTImageMapView = findViewById(R.id.imageMapView)
    mapView.setImageDrawable(usState)
    mapView.setTouchedMapReceiver(this)
    mapView.setShowPath(true)
    mapView.setPolygons(mapList)
```  

### Sample Image and Maps  

- A [US map](https://github.com/stkim1/view.MTImageMapView/blob/main/sample/US_States.gif) in size of 600 x 383 px.  
- The [polygons](https://github.com/stkim1/view.MTImageMapView/blob/main/sample/assets/us_states.json) (maps) of the US states in JSON.  

### Example App  

- [MTImageMapView Example](https://github.com/stkim1/MTImageMapViewExample)

### Limits  

- Delegate only receives the index of a map  
- Coordinates must be provided in pairs  
- At least 3 pairs of coordinates must be presented  
- No "rect", "circle" type map is supported. "Polygon" only at this time being  

## Credits  
- US states image and all coordinates are credited to [Illinois Center for Information Technology and Web Accessibility](http://html.cita.illinois.edu/text/map/map-example.php)  

## Version

_VER_ : 0.1  

_UPDATED_ : Mar. 1, 2024