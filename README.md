# MTImageMapView  
## 🏗️👷‍♂️ 2013 [iOS MTImageMapView](https://github.com/stkim1/MTImageMapView) Android port 🏡

An image view to select __a complex polygon__ (`map` henceforth) out of many. 
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

- From Android API 24  

## Library Setup  

- libs.versions.toml  

```toml
[versions]
stkim1MTImageMapView = "0.1"

[libraries]
stkim1-view-mtimagemapview = {group = "io.github.stkim1", name = "view-mtimagemapview", version.ref = "stkim1MTImageMapView"}
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
            "dial": 907,
            "desc": "Alaska State Polygon Map"
        },
        "vertices": [[58,294],[56,293],[55,292],[54,292],[51,294]]
    },
    {
        "vertices": [[163,213],[153,290],[131,288],[90,266]],
        "description": "An area map without an id"
    }
]
```
4. For the `id` field, you can use good-old string, object, or totally eliminate it if you want to. You can mix up with other fields such as `description`.  
5. Nonetheless, the __`vertices`__ field has to be there, and it should maintain coordinations in the aforementioned array format in #2. The field and format will be enforced in the future versions.  
6. Instantiate `MTImageMapView` and implement `MTImageMapTouch` interface. Then pass the map to the instance of `MTImageMapView`.  
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

- A [US states image](https://github.com/stkim1/view.MTImageMapView/blob/main/sample/US_States.gif) in size of 600 x 383 px.  
- The [polygon maps](https://github.com/stkim1/view.MTImageMapView/blob/main/sample/assets/us_states.json) of the US states in JSON.  

### Example App  

- [MTImageMapView Example](https://github.com/stkim1/MTImageMapViewExample)

### Limits  

- Delegate only receives the index of a map  
- Coordinates must be provided in pairs  
- At least 3 pairs of coordinates must be presented  
- No "rect", "circle" type map is supported. "Polygon" only at this time being  

## Credits  
- Dan Sunday's Fast Winding Number Algorithm
```txt
Copyright 2000 softSurfer, 2012 Dan Sunday
This code may be freely used and modified for any purpose
providing that this copyright notice is included with it.
SoftSurfer makes no warranty for this code, and cannot be held
liable for any real or imagined damage resulting from its use.
Users of this code must verify correctness for their application.

isLeft(): tests if the object's point (hence the point) is Left|On|Right of an infinite line.
Input   : the point, P0, and P1
Return  : >0 for the point is at the left of the line through P0 and P1
          =0 for the point is on the line
          <0 for the point is at the right of the line

fastWindingNumber(): winding number test for a point in a polygon
Input   : polygon = vertex points of a polygon V[n+1] with V[n]=V[0]
             point = a point
Return  : wn = the winding number (=0 only when the point is outside)

```

- US states image and all coordinates are credited to [Illinois Center for Information Technology and Web Accessibility](http://html.cita.illinois.edu/text/map/map-example.php)  

## Version

_VER_ : 0.1  
_UPDATED_ : Mar. 1, 2024