# dnd-5th-3-android
## 살까, 말까 혼자 고민하는 대신 다양한 사람들의 명쾌한 해답을 들을 수 있는 커뮤니티

### Architecture

<img width="600" height="400" src="https://user-images.githubusercontent.com/63637706/125196038-3c059200-e293-11eb-9c15-f491f107aa92.PNG"/>

### Tech stack

- Android Studio 4.2.1 
- Kotlin 1.5.2
- Gradle 4.2.2
- MVVM LiveData DataBinding
- REST API - Retrofit2

### Git convention

- Create issue<br>
- Create branch for each issue (feature/issue-#) -> eg) feature/issue-1<br>
- After develop merged, Delete branch<br>

### Commit convention

- [feature] : add new feature<br>
- [layout] : layout and xml<br>
- [bug] : error fix<br>
- commit message : #issue number {issue title} eg) #1 [layout] MainActivity<br>

### Xml convention

- (where)_(what)<br>
- textView → tv / EditText → et / RecyclerView → rv / ImageView → img / Button → btn<br>
- xml id : view _ (where) _ (what)
  eg) tv_main_nickname
- state list drawable → selector _ (where) _ (what)
  eg) selector_main_button
- shape drawable → border_(color) _ ([fill/line/fill_line]) _ (radius)
  eg) border_white_fill_12
- color → (color)_(colorCode)
  eg) white_ffffff
