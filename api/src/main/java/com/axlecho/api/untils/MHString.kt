package com.axlecho.api.untils

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun transferTime(timeStr: String): Long {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
    return sdf.parse(timeStr).time
}

fun tranferTimeManhuagui(timeStr: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    return sdf.parse(timeStr).time
}

fun tranferTimePica(timeStr: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.CHINA)
    return sdf.parse(timeStr).time
}

fun magicDecode(code: String) {
    val cx = org.mozilla.javascript.Context.enter()
    try {
        val scope = cx.initStandardObjects()
        var lib = "var LZString=(function(){var f=String.fromCharCode;var keyStrBase64=\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\";var baseReverseDic={};function getBaseValue(alphabet,character){if(!baseReverseDic[alphabet]){baseReverseDic[alphabet]={};for(var i=0;i<alphabet.length;i++){baseReverseDic[alphabet][alphabet.charAt(i)]=i}}return baseReverseDic[alphabet][character]}var LZString={decompressFromBase64:function(input){if(input==null){return\"\"}if(input==\"\"){return null}return LZString._0(input.length,32,function(index){return getBaseValue(keyStrBase64,input.charAt(index))})},_0:function(length,resetValue,getNextValue){var dictionary=[],next,enlargeIn=4,dictSize=4,numBits=3,entry=\"\",result=[],i,w,bits,resb,maxpower,power,c,data={val:getNextValue(0),position:resetValue,index:1};for(i=0;i<3;i+=1){dictionary[i]=i}bits=0;maxpower=Math.pow(2,2);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}switch(next=bits){case 0:bits=0;maxpower=Math.pow(2,8);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}c=f(bits);break;case 1:bits=0;maxpower=Math.pow(2,16);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}c=f(bits);break;case 2:return\"\"}dictionary[3]=c;w=c;result.push(c);while(true){if(data.index>length){return\"\"}bits=0;maxpower=Math.pow(2,numBits);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}switch(c=bits){case 0:bits=0;maxpower=Math.pow(2,8);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}dictionary[dictSize++]=f(bits);c=dictSize-1;enlargeIn--;break;case 1:bits=0;maxpower=Math.pow(2,16);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}dictionary[dictSize++]=f(bits);c=dictSize-1;enlargeIn--;break;case 2:return result.join(\"\")}if(enlargeIn==0){enlargeIn=Math.pow(2,numBits);numBits++}if(dictionary[c]){entry=dictionary[c]}else{if(c===dictSize){entry=w+w.charAt(0)}else{return null}}result.push(entry);dictionary[dictSize++]=w+entry.charAt(0);enlargeIn--;w=entry;if(enlargeIn==0){enlargeIn=Math.pow(2,numBits);numBits++}}}};return LZString})();String.prototype.splic=function(f){return LZString.decompressFromBase64(this).split(f)};"
        var code = "(function(p,a,c,k,e,d){e=function(c){return(c<a?\"\":e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)d[e(c)]=k[c]||e(c);k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1;};while(c--)if(k[c])p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c]);return p;}('n.o({\"p\":m,\"j\":\"k~l~\",\"q\":\"v.2\",\"w\":x,\"u\":\"4\",\"r\":[\"s.2.3\",\"t.2.3\",\"6.2.3\",\"5.2.3\",\"8.2.3\",\"9.2.3\",\"7.2.3\",\"f.2.3\",\"e.2.3\",\"i.2.3\",\"g.2.3\",\"b.2.3\",\"a.2.3\",\"d.2.3\",\"c.2.3\",\"P.2.3\",\"Q.2.3\",\"R.2.3\",\"O.2.3\",\"L.2.3\",\"M.2.3\",\"N.2.3\",\"W.2.3\"],\"V\":T,\"U\":S,\"K\":\"/B/h/C/4/\",\"D\":1,\"y\":\"\",\"z\":A,\"H\":0,\"I\":{\"J\":\"E-F\"}}).G();',59,59,'D7BWAcHNgdwUwEbmIGm8AMBGQe2rAAqYDY9MBWYjATnIHZyAOYgJjP0YBYnbWjWMm0mAZhCtGwBADsAhgFs4wQJHxgHE1AN5qBng0CV0YC65QFIqgXzdggSk1AskaAbuUAhboGkDQIxOgFk1AiIGAMI2AZqgwWIDKAWQASwAEsZSAARKQAXKXEAgBNxcACAY2AAMwCAGzgAZ3I+fAwxROk5FzcPAH0SQtiXOgxBajIEdIB7RIBrcsTkiTgAD3CASTiMOkESCjFwLOEACwBPGL7QPoAnGEgEYCzI8IBXHIAxbxCAdVA9gEUAUT7cU4pr+YBhAHFgcNwMFMvgcFW4IMJAFwn8AQA3YbbdLAGQxMjgCKzYgeFHCfCCDgYvJoRgMVhUDECRjCFJSdJZeSZCSpALArKzOBxABesykLQBEgAahggA='['\\x73\\x70\\x6c\\x69\\x63']('\\x7c'),0,{}))"
        val result = cx.evaluateString(scope, lib + "\n" + code, "<cmd>", 1, null)

        // Convert the result to a string and print it.
        System.err.println(org.mozilla.javascript.Context.toString(result))

    } finally {
        // Exit from the context.
        org.mozilla.javascript.Context.exit()
    }
}

@Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
fun HMACSHA256(data: ByteArray, key: ByteArray): String {
    val signingKey = SecretKeySpec(key, "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(signingKey)
    return bytesToHexString(mac.doFinal(data))
}

fun bytesToHexString(src: ByteArray): String {
    val stringBuilder = StringBuilder("")
    for (i in 0 until src.size) {
        val v = src[i].toInt() and 0xFF
        val hv = Integer.toHexString(v)
        if (hv.length < 2) {
            stringBuilder.append(0)
        }
        stringBuilder.append(hv)
    }
    return stringBuilder.toString()
}