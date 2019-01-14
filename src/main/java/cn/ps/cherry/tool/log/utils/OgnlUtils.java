package cn.ps.cherry.tool.log.utils;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
/**
 * ognl转换帮助类
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午6:42:34
 */
public class OgnlUtils {
	/**
	 * 解析ognl表达式，替换原始值
	 * @param ognlExpresion ognl表达式
	 * @param map 参数集合
	 * @return
	 * @throws OgnlException
	 */
   public static String paraseOgnl(String ognlExpresion,Map<String,Object> map) throws OgnlException {
	    Object expression = Ognl.parseExpression(ognlExpresion);
		OgnlContext context = new OgnlContext(map);
        Object string = Ognl.getValue(expression, context, context.getRoot());
		return string.toString();
   }
}
