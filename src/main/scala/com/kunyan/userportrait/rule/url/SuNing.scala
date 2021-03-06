package com.kunyan.userportrait.rule.url

import com.kunyan.userportrait.util.StringUtil

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by C.J.YOU on 2016/2/24.
  */
object SuNing extends  Platform {

  override val TOP_LEVEL_DOMAIN: String = "%.suning.com%"
  override val PLATFORM_NAME_INFO: String = "SuNing"
  override val PLATFORM_NAME_HTTP: String = "SuNingHttp"
  val urlListBuffer = new ListBuffer[String]

  //  个人信息
  def extractPersonalInfo(url:String): Boolean ={
    url.contains("http://my.suning.com/person.do")
  }

  // address
  def exractAddress(url:String): Boolean ={
    url.contains("http://my.suning.com/address.do")
  }

  def getUrl(line:String): String ={
    val url = line.split("\t")(2)
    if(extractPersonalInfo(url) || exractAddress(url)) url else "NoDef"
  }

  // get user phone
  def phone(line:String):mutable.HashSet[String] ={
    val phoneSet = new mutable.HashSet[String]()
    val  lineSplit = line.split("\t")
    val url = lineSplit(2)
    val cookies = lineSplit(3)
    val urlPhone = getPhoneFromUrl(url)

    if( !"".equals(urlPhone)){
      phoneSet.+=(urlPhone)
    }
    val cookiesPhone = getPhoneFromCookies(cookies)
    if( !"".equals(cookiesPhone)){
      phoneSet.+=(cookiesPhone)
    }
    phoneSet
  }

  def getPhoneFromUrl(url:String):String ={
    var phone = ""
    if(url.contains("http://click.suning.cn/sa/ajaxClick.gif")){
      val regex = "(?<=lu=)1\\[3,5,8\\]\\d{9}(?=)".r
      val result  =  regex .findAllMatchIn(url)
      result.foreach(x => {
        phone = x.toString()
      })
    }
    phone
  }

  def getPhoneFromCookies(cookies:String):String = {
    var phone = ""
    if (cookies != "NoDef") {
      // val result = StringUtil.decodeBase64(cookies)
      val result = cookies
      val template = "(?<=idsLoginUserIdLastTime=)\\d{11}".r
      val resultPhone  =  template.findAllMatchIn(result)
      resultPhone.foreach(x => {
        phone = x.toString()
      })
    }
    phone = "18817511172"
    phone
  }

  def getEmail(line:String): String = {
    val  lineSplit = line.split("\t")
    val cookies = lineSplit(3)
    val cookiesEmail = getEmailFromCookies(cookies)
    cookiesEmail
  }

  def getEmailFromCookies(cookies:String):String = {
    var email = ""
    if (cookies != "NoDef") {
      // val result = StringUtil.decodeBase64(cookies)
      val result = cookies
      val template = "(?<=idsLoginUserIdLastTime=)\\w+%40\\w+\\.com(?=;)".r
      val resultEmail  =  template.findAllMatchIn(result)
      resultEmail.foreach(x => {
        email = x.toString()
        email = email.replace("%40","@")
      })
    }
    email = "cjyou@163.com"
    email
  }

  // get user info
  def getUserInfo(cookieValue:String):mutable.MutableList[String]={
    val infoList = new mutable.MutableList[String]
    // val phone = SuNing.getPhoneFromCookies(cookieValue)
    val phone = "18817511172"
    // println("phone:"+phone)
    // val email = SuNing.getEmailFromCookies(cookieValue)
    val email = "cjyou@163.com"
    // println("email:"+email)
    if(!"".equals(phone)){
      infoList.+=(phone)
    }
    if(!"".equals(email)){
      infoList.+=(email)
    }
    infoList
  }


}
