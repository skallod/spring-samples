//package ru.galuzin.springrest.controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v2/common")
//public class CommonController {
//
//    @RequestMapping(
//            value = ["/**"],
//    method = [
//    RequestMethod.GET,
//    RequestMethod.POST
//        ]
//                )
//    fun httpRequest(
//            @RequestHeader headers: MutableMap<String, String>,
//            @RequestParam requestParams: MutableMap<String, String>,
//            @RequestBody(required = false) requestBody: String?,
//            httpServletRequest: HttpServletRequest
//    ): ResponseEntity<String> {
//        log.info("CcCustomRestController is processing request: {}", httpServletRequest.requestURL);
//        return CcRequestRest(
//                url = httpServletRequest.requestURI,
//                method = httpServletRequest.method,
//                headers = headers,
//                params = requestParams,
//                body = requestBody?.let { ObjectMapperUtils.readValueToMapAny(it) } ?: emptyMap()
//        )
//            .let { ccCustomRestService.handleRequest(it) }
//            .let { getResponseEntity(it) }
//
//    }
//}
