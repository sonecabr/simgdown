

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class DownloadMain {
	
	
	
	public static void main(String args[]){
		String actualFile = null;
		int errorCount = 0;
		int fileCount = 0;
		
		List<Bean> newCollection = new ArrayList<Bean>();
		try{
			String json = args[0];
			String outputFolder = args[1];
			String newUrl = null;
			if(args.length > 2){
				newUrl = args[2];
			}
			
			
			DataInputStream ins = new DataInputStream(new FileInputStream(json));
			byte[] b = new byte[ins.available()];
			ins.readFully(b);
			ins.close();
			
			String jsonString = new String(b, 0, b.length);
			
			
			
			
			List<Bean> jsonList = new JSONDeserializer<List<Bean>>()
					.use(null, ArrayList.class)
					.use("values", Bean.class)
					.deserialize(jsonString);
			
			
			for(Bean bean : jsonList){
				actualFile = bean.getUrl();
				String fileName = bean.getUrl().split("/")[bean.getUrl().split("/").length -1];
				File outFile = new File(outputFolder + "/" + fileName);
				
				try{
					URL url = new URL(bean.getUrl());
					URLConnection conn = url.openConnection();
					InputStream is = conn.getInputStream();
					BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outFile));
					int len;
					byte[] buf = new byte[1024];
					while((len = is.read(buf)) > 0){
						os.write(buf, 0, len);
					}				
					os.close();
					is.close();
					fileCount++;
					
					if(newUrl != null){
						Bean beanNew = new Bean();
						beanNew.setId(bean.getId());
						beanNew.setUrl((newUrl.endsWith("/") ? newUrl : newUrl +"/") +  outFile.getName());
						newCollection.add(beanNew);
					}
					
					
					System.out.println("Arquivo baixado : " + url);
					
					
					
					
					
				} catch (MalformedURLException e) {	
					System.out.println("Erro ao baixar arquivo : " + actualFile);
					errorCount++;
					//e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Erro ao baixar arquivo : " + actualFile);
					errorCount++;
					//e.printStackTrace();
				}
				
				
				
			}
			
			if(newUrl != null){
				JSONSerializer serial = new JSONSerializer();
				String jsonR = serial.exclude("*.class").serialize(newCollection);
				
				
				FileOutputStream fO = new FileOutputStream(new File(outputFolder + "newJson.json"));
				BufferedOutputStream bfo = new BufferedOutputStream(fO);
				bfo.write(jsonR.getBytes());
				bfo.close();
				fO.close();
				
				System.out.println("Novo json gerado em :: " + outputFolder + "/newJson.json");
				System.out.println("\nTotal de arquivos com erro:" + errorCount );
				System.out.println("\nTotal de arquivos com sucesso:" + fileCount );
			}
			
			System.out.println("Processo concluido!");
			
		} catch (NullPointerException e){
			System.out.println("Erro::parametro json[0] ou pasta de saida [1] nao fornecido");
			System.out.println("O arquivo json deve ter o padrao [{\"id\":\"ID\", \"url\":\"URL\"},{\"id\":\"IDn\", \"url\":\"URLn\"}]");
			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Erro::parametro json[0] ou pasta de saida [1] nao fornecido");
			System.out.println("O arquivo json deve ter o padrao [{\"id\":\"ID\", \"url\":\"URL\"},{\"id\":\"IDn\", \"url\":\"URLn\"}]");
		} catch (FileNotFoundException e1) {
			System.out.println("Erro::parametro json[0] ou pasta de saida [1] nao fornecido");
			System.out.println("O arquivo json deve ter o padrao [{\"id\":\"ID\", \"url\":\"URL\"},{\"id\":\"IDn\", \"url\":\"URLn\"}]");
		} catch (IOException e1) {
			System.out.println("Erro::parametro json[0] ou pasta de saida [1] nao fornecido");
			System.out.println("O arquivo json deve ter o padrao [{\"id\":\"ID\", \"url\":\"URL\"},{\"id\":\"IDn\", \"url\":\"URLn\"}]");
		}
		
	}

}
