

<h1 align="center">
  <a href="https://github.com/33ohoh/android" title="AwesomeCV Documentation">
    <img alt="AwesomeCV" src="https://github.com/stock-price-calculator/tradingbot/assets/77156858/f88c394a-6d15-46ce-9321-35b6893f4963" width="200px" height="200px" />
  </a>
  <br />
  Past Pest
</h1>
<p align="center">
  병해충 정보 및 피해현황 파악을 위한 애플리케이션입니다.

</p>

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=Android&logoColor=white"/>

  <img src="https://img.shields.io/badge/Node.js-339933?style=flat&logo=nodedotjs&logoColor=white"/>

<img src="https://img.shields.io/badge/javascript-F7DF1E?style=flat&logo=javascript&logoColor=white"/>

<!-- <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/> -->

<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat&logo=amazons3&logoColor=white"/>

<img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat&logo=amazonrds&logoColor=white"/>

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>

 
</div>

<br />

## 📌 개요
- 프로젝트 이름 : Past Pest
- 참여 공모전 : 2022년  산림공공∙빅데이터 활용 창업경진 대회
- 개발 배경 : 병해충 피해현황 파악조차 제대로 확인할 수 없다는 문제점을 발견하였습니다. 이를 해결하기 위해서 병해충 피해예방 및 관련정보 제공을 위한 안드로이드 앱을 제작하였습니다. (<a href="https://www.nongmin.com/342133">뉴스링크 </a>)
- 개발 언어 : Java, Javascript
- 프론트 : Android
- 백엔드 : Node.js, RDS, S3, MySQL
    
## 🖥️ 프로젝트 소개
- 병충해 피해는 해마다 증가하고 있지만 현황 파악에 어려움이 있습니다. 과거의 데이터와 사용자의 신고 데이터를 활용하여 미리 방제 대책을 세울 수 있도록 도움을 주는 서비스인 PastPest를 개발하게 되었습니다.
- 농업진흥청, 한국임원진흥원, 국가농작물 병해충 관리시스템에서 제공하는 공개 API를 사용하였습니다.
- AWS를 통해서 Node.js를 배포를 하였고, RDS의 Mysql 그리고 S3를 이용하여 이미지를 저장했습니다.

## 🕰️ 개발 기간
-  2022.06.27 ~ 2022.08.07

## 👬 팀 소개
- 홍영환 - <a href="https://github.com/33ohoh/pestback">백엔드 (Node.js)</a>, 서버 구축 및 AWS 배포 , <a href="https://github.com/33ohoh/android">Android 앱 </a> ( 로그인, 회원가입, 병해충 현황파악 페이지, 마이페이지, 페이지 이동 메뉴,   다른 페이지간 이동)  <br>
- 조형준 - <a href="https://github.com/33ohoh/android">Android 앱 </a>(신고하기 페이지, 병해충 현황 파악 페이지, 병해도감 )
- 김민지 - <a href="https://github.com/33ohoh/android">Android 앱</a> (이달의 병해충 예보 페이지)
## ⚙️ 프로젝트 주요 기능
### 1. 병해충 예보
- 병해충 예보는 1년 중 특정 시기를 선택하여 해당 시기에 유의해야 할 병해충 정보를 제공하는
기능입니다.
시기를 선택한 후 결과 조회 버튼을 누르면 시기별로 예보가 발령된 작물의 리스트가 최신화됩니다. 이후 특정 작물을 누르면 해당 작물과 관련된 병해충의 리스트가 경보 정도와 함께 보여집니다.
특정 병해충을 선택하게 되면 해당 병해충에 대한 특징,증상,방제법에 대한 정보를 조회할 수 있도록 구성하였습니다.
### 2. 병해충 신고
- 병해충 신고는 임업 종사자가 겪은 피해 내용을 신고할 수 있도록 하는 내용입니다.
병해충 신고를 위해 위치정보, 임산물정보, 증상정보, 해충정보, 사진정보, 신고제목, 세부내용을 담을 수 있습니다. 
위치등록 버튼을 누르면 위치를 등록할 수 있는 페이지가 나타납니다. 주소 검색을 통해 특정 위치를 검색하거나 현재 위치 찾기 버튼을 눌러 현재 위치로 이동할 수 있도록 하였습니다. 이후 지도를 직접 움직이고 하단에 세부주소를 적어 정확하고 자세한 위치를 등록할 수 있도록 하였습니다. 
### 3. 병해충 현황 파악
- 다른 종사자들이 신고한 내역을 토대로 병해충 발생 현황을 파악할 수 있도록 하였습니다.
지도로 보기 버튼을 누르면 지도상에 다른 종사자들이 신고한 위치가 보이게 하여 어느 장소에서 병해충이 발생하고 어느정도 퍼졌는지 시각적으로 파악할 수 있도록 하였습니다.
이후 게시물을 클릭하게 되면 신고 세부내역을 차례대로 파악할 수 있도록 구현하였습니다.

### 4. 임산물별 병해충 정보조회 
- 먼저 정보 조회를 원하는 임산물을 선택하여 정보 조회버튼 누릅니다. 이후 질병정보와 해충정보중 하나를 선택할 수 있습니다. 선택을 하게되면 각각 처음에 선택한 임산물과 관련된 
질병 리스트와 해충 리스트를 보여주고 해당 질병이나 해충에 대한 특징,증상,방제법을 조회 할 수 있습니다.

### 5. 마이페이지
- 비밀번호 변경, 로그아웃, 회원탈퇴를 눌러 계정 정보를 수정하거나 삭제할 수 있습니다.
병해충 피해 신고내역을 누르면 이용중인 사용자가 신고한 내역 리스트가 나오게 만들었습니다. 그 중 하나를 선택하게 되면 신고 세부내역을 파악할 수 있도록 구현하였습니다.
</hr>



## 🛠️ 아쉬웠던점
- 구글 플레이 스토어 앱 업로드 이전 검사하는 과정에서 막혀 출시까지는 못했던 것이 가장 아쉬었던 것 같습니다.

## 😀 기대효과
- 공공데이터를 활용해 사용자에게 병해충 정보를 제공하고, 피해 신고 기능을 통해 수집된 데이터는 또 하나의 공공데이터로써 다음 해의 방제계획을 세우는 데 도움을 줄 것으로 기대합니다.

# Background
<img src="https://user-images.githubusercontent.com/102522202/223727268-74795c11-5958-46b0-89b9-4b17f73bb6fd.png"/>

-----------------------------------
# Features
<img width="1122" alt="image" src="https://user-images.githubusercontent.com/102522202/223730961-e55492f1-e91c-403f-80f8-a4815b71e144.png">
<img width="1123" alt="image" src="https://user-images.githubusercontent.com/102522202/223731080-579f0fff-cdd6-4dc4-825e-6c1ae797ac9e.png">
<img width="1121" alt="image" src="https://user-images.githubusercontent.com/102522202/223731150-66fa59f6-a77e-46d2-819a-fdf6156a3ed6.png">
<img width="1124" alt="image" src="https://user-images.githubusercontent.com/102522202/223731258-c3ffbd7e-e6a5-49b9-8e5b-63201fcff86e.png">
<img width="1122" alt="image" src="https://user-images.githubusercontent.com/102522202/223731422-bc33abda-6126-4881-812f-d945bbd9637c.png">


-----------------------------------
# Architechture
<img width="1116" alt="image" src="https://user-images.githubusercontent.com/102522202/223727915-c51a78a7-e054-43e9-b185-3ec46ce89559.png">

-----------------------------------
# In Mobile

### 병해충 예보
<img width="1114" alt="image" src="https://user-images.githubusercontent.com/102522202/223728580-66da0635-1153-4f20-adf6-000c95476187.png">

### 병해충 정보 조회
<img width="983" alt="image" src="https://user-images.githubusercontent.com/102522202/223729169-f1fcd004-02f7-43b3-becb-502c96075b92.png">

### 병해충 신고

### 병해충 현황 파악
<img width="1120" alt="image" src="https://user-images.githubusercontent.com/102522202/223728896-0e2de033-4a27-4a33-b81b-9a98e9767258.png">

