package spring.yolo.controller;

import java.nio.charset.Charset;
import java.util.Random;

import org.json.*;
import org.json.JSONObject;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.*;
import org.apache.hc.client5.http.entity.mime.*;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

@Controller
public class YoloController {
		
			@ResponseBody
			@RequestMapping(value="/sendImage.do",
							method=RequestMethod.POST)
			public String sendImageToRestServer(
						@RequestBody String data) throws Exception{
				
				//data에서 data:삭제
				data = data.replace("data:", "");
				//System.out.println("data=" +data);
				
				JSONObject dataObject = new JSONObject(data);
				//System.out.println("dataObject" + dataObject);
				
				String cam_data = ((String) dataObject.get("img_data"));
				//System.out.println("cam_data=" + cam_data);
				
				JSONObject restSendData = new JSONObject();
				restSendData.put("data", cam_data);
				//System.out.println("restSendData=" + restSendData);
				
				HttpPost httpPost = new HttpPost("http://localhost:5000/detect");
				
				httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
				
				StringEntity StringEntity = new StringEntity(
													restSendData.toString());
				
				httpPost.setEntity(StringEntity);
				
				CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse response2 =httpclient.execute(httpPost);
				
				String yolo_message = EntityUtils.toString(
											response2.getEntity(),
											Charset.forName("UTF-8")
											);
				
				//System.out.println("yolo_message=" + yolo_message);
				
				return yolo_message;
			}
					
				
			@ResponseBody
			
			@RequestMapping(value="/webcamControl.do",
							method=RequestMethod.POST)
			
			public String webcam(@RequestBody String data) {
				
//				String webcam_img = request.getInitParameter("img_data");
//				System.out.println("webcam_img="+webcam_img);
				
				System.out.println("data=" + data);
				
				Random rand = new Random();
				
				int t = rand.nextInt(100);
				int l = rand.nextInt(100);
				int r = rand.nextInt(100);
				int b = rand.nextInt(100);
				
				JSONObject o1 = new JSONObject();
				
				o1.put("left", l );
				o1.put("top", t );
				o1.put("right", r );
				o1.put("bottom", b );
				o1.put("cls", 0 );
				o1.put("conf", 0.8 );
				
				System.out.println("o1.toString()="+o1.toString());
				
				JSONArray arr = new JSONArray();
				arr.put(o1);
				System.out.println("arr.toString()="+arr.toString());
				
				
//				ModelAndView mav = new ModelAndView();
//				mav.addObject("result", arr.toString());
//				
//				return mav;
				return arr.toString();
			}
			@RequestMapping(value="/webcamControl.do",
					method=RequestMethod.GET)
			public ModelAndView insertBookForm() {
				
				ModelAndView mav = new ModelAndView();
				mav.setViewName("webcam.html");
				
				return mav;
			}
			
}
