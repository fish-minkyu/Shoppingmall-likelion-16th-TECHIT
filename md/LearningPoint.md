# Learning Point

<details>
<summary><strong>프론트에서 Form 태그의 목록 상자는 서버에 어떻게 데이터를 보내줄까?</strong></summary>

### `Background.`

API를 만들던 도중, 클라이언트에서 목록 상자 중, 하나를 택해 데이터를 보내면  
서버는 어떤 형식으로 받게 되는 것인지를 알아야 데이터를 받을 수 있으므로 궁금해졌다.

### `Answer.`

사용자의 선택을 서버에 전달하기 위해 HTML에서 `<form>`태그와 `<select>`태그를 사용할 수 있다.

```html
<!DOCTYPE html>
<html>
<head>
    <title>클라이언트</title>
</head>
<body>
    <form action="/example.com/items/create" method="POST">
        <label for="title">제목:</label><br>
        <input type="text" id="title" name="title"><br>

        <label for="content">내용:</label><br>
        <textarea id="content" name="content"></textarea><br>

        <label for="status">상태:</label><br>
        <select id="status" name="status">
            <option value="중고">중고</option>
            <option value="미개봉">미개봉</option>
            <option value="새상품">새상품</option>
        </select>
        
        <input type="submit" value="전송">
    </form>
</body>
</html>

```

이 때, GET요청인지 POST 요청인지에 따라 달라지는데  
GET 요청이면 모든 데이터들이 쿼리 파라미터로 서버에 전송이 되고,  
POST 요청이면 body로 서버에 전송이 된다.

</details>

<details>
<summary><strong>승인 or 거절을 결정하는 API에서 결정 여부는 Param으로 받아야 하나 Body로 받아야 하나?</strong></summary>

### `Background.`

쇼핑몰 “개설 신청” 수락 또는 거절을 처리할 때, 수략여부를 Param으로 받아야 할지  
Body로 받아야 할지 고민이 있었다.  
Param으로 받으면 DTO를 따로 만들지 않아도 되어서 간편했으나 보안이 걱정이었고  
Body로 하자니 이게 정말 맞는지 헷갈렸다.

### `Answer.`

```java
  @PutMapping("/approval/{shopId}")
  public ManagementShopDto approval(
    @PathVariable("shopId") Long shopId,
    @RequestBody DecesionDto dto
    ) {
    return adminService.approvalOrRefuse(shopId, dto);
  }
```

결론은 위와 같이 Body로 받아주는 것이 맞다.  
승인이나 거절 같은 결정적인 정보는 주로 HTTP 요청의 본문(body)으로 전송한다.  
그 이유는 2가지가 있다.

1. 데이터의 보안


: URL의 매개변수(param)로 데이터를 전송하면, 해당 데이터는 URL에 노출되어 로그에 기록되거나  
브라우어의 히스토리에 저장될 수 있다. 이런 점에서 중요한 데이터는 본문(body)를 통해 전송하는 것이 좋다.

2. 데이터의 복잡성


: 본문(body)를 통해 전송하면 복잡한 데이터 구조를 쉽게 전송할 수 있다.

</details>

<details>
<summary><strong>이미지를 요청받을 때, RequestParam이 아닌 DTO를 이용한 @RequestBody는 안될까?</strong></summary>

### `Background.`

이미지를 받고 저장하면서 문득 굳이 따로 @RequestParam으로 받아줘야 할지가 궁금해졌다.

### `Answer.`

DTO를 통해 이미지를 전송하는 것은 일반적으로 권장되지 않는다. 그 이유는 다음과 같다.

1. 이미지 파일은 보통 크기가 크기 때문에, 이를 Base64 등의 형태로 인코딩하여 JSON 형태의  
   DTO에 포함시키면, 전송해야 할 데이터의 크기가 급격히 증가하게 된다.  
   이로 인해 네트워크 부하가 증가하고 응답 시간이 길어질 수 있다.
2. 이미지를 DTO에 포함시키면, 이미지 데이터가 문자열 형태로 변환이 되어야 한다.  
   이 과정에서 데이터 변환 오류가 발생할 가능성이 있다.

따라서, 이미지 파일을 서버에 전송할 때는 일반적으로 MultipartFile을 사용하는 것이 좋다.  
MultipartFile을 사용하면, 이미지 파일을 그대로 전송할 수 있으므로 위에서 언급한 문제를 피할 수 있다.

</details>

<details>
<summary><strong>@ModelAttribute를 이용해 이미지 파일과 body 데이터를 한꺼번에 처리하기</strong></summary>

### `Background.`

Used를 Create할 때, 이미지와 Used에 관한 데이터를 한번에 받아 처리하고 싶었다.  
처음에는 Form 데이터가 서버로 올 때, 이미지 파일은 @RequestParam으로 받고  
나머지는 body로 올 것이라 생각하고 @RequestBody를 동시에 사용했으나  데이터를 받을 수 없었다.

```java
  @PutMapping("/modifying/{usedId}")
  public ResponseItemDto updateItem(
    @PathVariable("usedId") Long usedId,
    @RequestBody RequestItemDto dto,
    MultipartFile usedImage
  ) {
    return usedService.updateItem(usedId, dto, usedImage);
  }
```

```java
2024-03-04T23:11:41.251+09:00  WARN 4251 --- [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: Content-Type 'multipart/form-data;boundary=--------------------------293556967219885732684779;charset=UTF-8' is not supported]
```

또 다른 방법으론, 이미지를 DTO에 포함시켜서 받아볼까 했으나,  
이미지 같은 Binary 데이터는 일반적인 텍스트 데이터에 비해 크기가 크기 때문에 성능 저하를 일으킬 수 있고  
인코딩 문제가 있었다.

### `Answer.`

@ModelAttribute를 이용해 상황을 해결할 수 있었다.

```java
  @PutMapping("/modifying/{usedId}")
  public ResponseItemDto updateItem(
    @PathVariable("usedId") Long usedId,
    @ModelAttribute RequestItemDto dto,
    @RequestParam("usedImage") MultipartFile usedImage
  ) {
    return usedService.updateItem(usedId, dto, usedImage);
  }
```

@ModelAttribute은 Spring MVC에서 사용되는 어노테이션이다.  
주로 2가지 방식으로 사용된다.

1. 메소드 레벨에서 사용

컨트롤러 내의 특정 메서드에 `@ModelAttribute`을 붙이면,  
해당 메소드가 모델 데이터를 생성 or 수정하는 역할을 하게 된다.

이 메소드는 @RequestMapping 어노테이션이 붙은 메소드보다 먼저 호출되어,  
모든 요청에 대해 공통적으로 모델 데이터를 준비할 수 있다.  
예를 들어, 모든 페이지에서 필요한 사용자 정보나 설정 정보를 로드하는 경우에 사용할 수 있다.

```java
@ModelAttribute
    public void commonModel(Model model) {
        model.addAttribute("message", "Welcome to Our Website!");
    }
```

2. 메소드 파라미터 레벨에서 사용

컨트롤러의 핸들러 메소드(요청을 처리하는 메소드)의 파라미터에 @ModelAttribute를 붙이면  
Spring MVC는 해당 파라미터의 타입에 맞는 객체를 생성하고,
요청 파라미터를 바인딩(요청 파라미터의 이름과 객체의 이름이 일치할 때 자동으로 값을 설정)한다.

이렇게 생성 및 바인딩 된 객체는 자동으로 모델에 추가되어 뷰에서 사용할 수 있다.  
여기서 @ModelAttribute을 사용하기 위해선 단 2가지 주의사항이 있다.

1. 클라이언트의 form 태그의 input 필드 이름이 DTO 클래스의 필드 이름과 일치해야 한다.
2. DTO 클래스에 기본 생성자 또는 Setter가 있어야 한다.

</details>


<details>
<summary><strong>검색 시, 검색어는 쿼리스트링으로 전달하는 이유</strong></summary>

검색어가 쿼리 스트링을 통해 전달되는 이유는 HTTP 프로토콜의 특성과 웹 브라우저 동작 방식 때문이다.
HTTP 프로토콜은 클라이언트와 서버 간의 요청과 응답을 처리하는 통신 규약이다.
이 규약에 따르면, 정보를 요청할 때 URL을 이용해 서버에 전달하게 되는데,
이 때 요청의 세부 사항을 명시하기 위해 쿼리 스트링이 사용된다.  

쿼리 스트링은 웹 페이지의 URL에 추가되는 특정 형식의 문자열로, 서버에게 추가 정보를 제공하는 역할이다.  
이 정보는 웹 페이지의 내용을 변경하거나 특정 작업을 수행하도록 명령하는데 사용될 수 있다.  
쿼리 스트링은 URL의 끝에 위치하며 `?`로 시작하고, `&`로 각각의 파라미터를 구분한다.  

예를 들어, `www.example.com/search?query=파스타` 라는 URL에서
`query=파스타` 부분이 쿼리 스트링이다.  
`query`라는 키와 파스타라는 값을 가지며, 서버는 사용자가 `파스타`를 검색하길 원하는 것을 알 수 있다.  

또한, 웹 페이지의 정렬 순서를 변경하고 싶을 때도 쿼리 스트링을 사용한다.  
예를 들어, 최신순으로 정렬하고 싶다면 `?sort=newest`와 같은 쿼리 스트링을 사용하면 된다.  
검색어를 쿼리 스트링에 담아 전달하면, 서버는 이를 해석하여 해당 검색어에 맞는 결과를 반환한다.  
이런 방식은 사용자가 원하는 정보를 특정하고, 서버가 그에 따른 적절한 응답을 제공하는데 매우 효과적이다.

</details>

<details>
<summary><strong>검색 API 구현 방법</strong></summary>

### `Background.`

검색 API 구현 시, 구체적으로 코드를 어떻게 짜야 하는지 잘 모르겠다.  
예를 들어, 이름과 가격 범위를 기준으로 검색을 한다 했을 때  
이름만 검색하는 API 따로 가격 범위만 검색하는 기준을 따로 줘야 할지  
또는 한꺼번에 처리가 가능한지가 감이 잘 잡히지가 않았다.

### `Answer.`

검색 API는 GET 요청을 받아 처리한다.

```java
 @GetMapping("shops")
    public Page<ShopDto> searchShops(
            ShopSearchParams params,
            Pageable pageable
    ) {
        log.info("{}", params);
        return service.searchShops(params, pageable);
    }

    @GetMapping("items")
    public Page<ShopItemDto> searchItems(
            ItemSearchParams params,
            Pageable pageable
    ) {
        log.info("{}", params);
        return service.searchItems(params, pageable);
    }
}
```

GET 요청일 때, form 데이터를 서버로 보내면 쿼리 파라미터로 전송을 하게 된다.  
하지만, API를 보면 데이터를 DTO를 통해 받고 있다. 이 때, 이런 의문이 들 수 있다.  
어떻게 데이터를 받고 있는 것인가? 설마 body로 데이터를 받고 있는 것인가?  
Spring 프레임워크는 @RequestParam, @PathVariable, @RequestHeader 등 다양한 방법을 통해  
GET 요청의 데이터를 받을 수 있지만, DTO를 사용하는 경우에는 @ModelAttribute가 기본적으로 적용된다.

이 어노테이션은 클라이언트가 보낸 요청의 파라미터들을 객체에 바인딩해주는 역할을 한다.  
따라서, 클라이언트는 URL에 쿼리 파라미터를 포함시켜 데이터를 전송하고, 서버는 @ModelAttribute로  
데이터를 받을 수 있는 것이다.

```java
GET /items?name=아이템명&price=10000
```

```java
    @GetMapping("items")
    public Page<ShopItemDto> searchItems(
            ItemSearchParams params, // @ModelAttribute 생략
            Pageable pageable
    ) {
        log.info("{}", params);
        return service.searchItems(params, pageable);
    }
```

</details>