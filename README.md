# 오늘하루 

## 하루 일정을 간편하게 관리할 수 있는 앱입니다.

[![Video Label](http://img.youtube.com/vi/iNutTwS1u80/0.jpg)](https://www.youtube.com/watch?v=iNutTwS1u80)

이미지 클릭하시면 app preview 영상을 보실 수 있습니다.


## 개요

- 개발기간 : '20.11.26 ~ '21.1.15 (약 2개월)
- 개발환경 : 안드로이드 스튜디오 (v. 4.1)
- 개발언어 : Kotlin
- SDK : min 26 ~ target 30


## 기술스택

- MVVM (architecture) 

- Single Activity(with conductor) 

- AAC ViewModel 

- AAC Databinding 

- AAC Livedata 

- Room (Database)

- Coroutine (RX Programming)

- Dagger2 (Dependency Injection)

- Clean Architecture 일부 적용 (UseCase Layer는 두지 않았습니다..)


## 아키텍처
![alt text](https://github.com/GodDB/PlanPlusMinus/blob/master/todayTodo-architecture.png?raw=true)

### Data Flow

- VM (데이터 요청) -> Repository (데이터 요청) -> DataSource or SharedPreferenceManager (데이터 요청)
   -> DB or Shared Preference (데이터 요청 후 데이터 전달)
  -> DataSource or SharedPreferenceManager(데이터 전달) -> Repository에서 Mapper를 통해 데이터 변환 (Data -> Entity)
  -> VM에서 Mapper를 통해 데이터 변환 (Entity -> ViewData) -> View에 데이터 변경을 알림 -> View 갱신
  
  * 이 과정은 Coroutine의 Flow api를 사용하여 데이터 스트림 형태로 전달 받습니다. (즉, 데이터 변경 시 변경을 감지해 위 과정이 자동으로 처리됨) 
  

