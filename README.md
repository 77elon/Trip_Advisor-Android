# Trip_Advisor-Android
 2022-1 캡스톤 프로토타입 애플리케이션
 여행 경로 추천 애플리케이션을 컨셉으로 혼자 제작해봤던 안드로이드 애플리케이션입니다.
 
 <구현 환경>
 
 NaverMap API를 사용해 지도 부분을 표시할 수 있고, 현재 위치를 감지할 수 있습니다.
 구현 당시, 네이버 플레이스 API의 검색 기능이 미흡하여, 여행지 추천 애플리케이션에 사용하는 것은 부적절하다고 판단.
 Kakao Place를 채택하여, 현 위치 기반으로 가까운 순으로 건물 위치를 검색 할 수 있습니다.
 RetroFit2을 사용하여, Kakao Place 검색 데이터를 불러오도록 구현하였습니다. (Json Request -> Document Object -> Toolbar)
 
 
 <구현 기능!>
 
 * [현재 위치 표시]


  ![현재 위치 추적](https://user-images.githubusercontent.com/88618717/192143334-da63bcb2-a6ca-4619-bd44-fcfcf3f650c1.png)
  
  사용자에게 여행 경로를 추천하기 위한 현 위치 추적 기능입니다.
  
 * [건물 정보 표시]


  ![건물 정보 표시](https://user-images.githubusercontent.com/88618717/192143338-f036bf33-f193-457a-ae5e-b98af0958602.png)
  
  사용자가 지도를 움직이며, 건물을 터치한다면 건물의 이름과 정보가 표시됩니다.

 * [위치 기반 인근 건물 검색]


  ![거리 순 검색](https://user-images.githubusercontent.com/88618717/192142652-ba6f8cbc-7f32-4239-bb0a-0433e03ea81d.png)
  
  입력한 검색어와 가장 근접한, 위치 상으로도 가까운 순으로 건물 검색 결과를 보여줍니다.
  
 * [거리 계산]


  ![검색 결과](https://user-images.githubusercontent.com/88618717/192143446-fca35c5e-7a84-4bef-bda1-b6caf110a30a.png)
  
  사진에서는 현 위치인 삼척해수욕장에서 삼척 쏠비치를 검색했으며, 상호 명, 주소, 전화번호, 거리를 보여줍니다.
