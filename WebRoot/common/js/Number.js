//javascript在两个浮点数运算的时候会有比较明显的误差。增加下列方法解决。
//2009-07-18 Skyeah

/**
 * 除法函数，用来得到精确的除法结果 
 * javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。 
 * @param {} arg1
 * @param {} arg2
 * @return {} arg1除以arg2的精确结果
 */
function accDiv(arg1, arg2) {
    var t1 = 0, t2 = 0, r1, r2;
    try { t1 = arg1.toString().split(".")[1].length } catch (e) { }
    try { t2 = arg2.toString().split(".")[1].length } catch (e) { }
    with (Math) {
        r1 = Number(arg1.toString().replace(".", ""))
        r2 = Number(arg2.toString().replace(".", ""))
        return (r1 / r2) * pow(10, t2 - t1);
    }
}
/**
 * 给Number类型增加一个除法函数，调用起来更加方便。
 * @param {} arg
 * @return {}
 */ 
Number.prototype.div = function(arg) {
    return accDiv(this, arg);
}
/**
 * 乘法函数，用来得到精确的乘法结果 
 * 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。 
 * @param {} arg1
 * @param {} arg2
 * @return {} arg1乘以arg2的精确结果 
 */
function accMul(arg1, arg2) {
    var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
    try { m += s1.split(".")[1].length } catch (e) { }
    try { m += s2.split(".")[1].length } catch (e) { }
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
}
/**
 * 给Number类型增加一个乘法函数，调用起来更加方便。 
 * @param {} arg
 * @return {}
 */
Number.prototype.mul = function(arg) {
    return accMul(arg, this);
}
/**
 * 加法函数，用来得到精确的加法结果 
 * 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
 * @param {} arg1
 * @param {} arg2
 * @return {} arg1加上arg2的精确结果 
 */
function accAdd(arg1, arg2) {
    var r1, r2, m;
    try { r1 = arg1.toString().split(".")[1].length } catch (e) { r1 = 0 }
    try { r2 = arg2.toString().split(".")[1].length } catch (e) { r2 = 0 }
    m = Math.pow(10, Math.max(r1, r2));
    return (Number(arg1).mul(m) + Number(arg2).mul(m)) / m;
}
/**
 * 给Number类型增加一个加法函数，调用起来更加方便。 
 * @param {} arg
 * @return {}
 */
Number.prototype.add = function(arg) {
    return accAdd(arg, this);
}

/**
 * 减法函数，用来得到精确的减法结果
 * 说明：javascript的减法结果会有误差，在两个浮点数相减的时候会比较明显。这个函数返回较为精确的加法结果。
 * @param {} arg1
 * @param {} arg2
 * @return {} arg1减去arg2的精确结果
 */
function accSub(arg1, arg2) {
    var r1, r2, m, n;
    try { r1 = arg1.toString().split(".")[1].length } catch (e) { r1 = 0 }
    try { r2 = arg2.toString().split(".")[1].length } catch (e) { r2 = 0 }
    m = Math.pow(10, Math.max(r1, r2));
    //n = (r1 >= r2) ? r1 : r2;
    //return ((arg1 * m - arg2 * m) / m).toFixed(n);
   return (Number(arg1).mul(m) - Number(arg2).mul(m)) / m;
}
/**
 * 给Number类型增加一个减法函数，调用起来更加方便
 * @param {} arg
 * @return {}
 */
Number.prototype.sub = function(arg) {
    return accSub(this,arg);
}
