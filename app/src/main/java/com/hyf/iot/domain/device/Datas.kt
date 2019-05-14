package com.hyf.iot.domain.device

data class Datas(var id: String,var str1: String,var str2:String, var str3:String, var str4:String,var str5: String,var str6: String,var valves: ArrayList<Valves>)

data class Valves(var valveId: String,var valveName: String,var state: Int)