package com.ikkong.core.modules.beetl.function;

import java.io.IOException;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.ikkong.core.toolbox.Func;

public class getDictName implements Function {

	@Override
	public Object call(Object[] paras, Context ctx) {
		  Object code = paras[0];
		  Object num = paras[1];
		  
          if (code != null&&num!=null){
        	  return Func.getDictName(code,num);
//                  try{
//                          ctx.byteWriter.writeString(Func.getDictName(code,num));
//                  }catch (IOException e){
//                          throw new RuntimeException(e);
//                  }
          }
          return "";
	}

}
