package taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
//import org.junit.Test;

public class FastdfsTest {
	
//	@Test
	public void testUpload() throws Exception, Exception {
		//1.添加jar包到工程中
		//2.初始化全局配置。加载配置文件
		ClientGlobal.init("D:\\code\\soa_taotao\\taotao-content\\taotao-content-service\\src\\main\\resources\\properties\\client.conf");
		//3.创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//4.创建一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//5.创建一个StorageServer对象，值 为Null
		StorageServer storageServer = null;
		//6.获得StorageClient对象
		StorageClient storageClient = new StorageClient();
		//7.直接调用StorageClient对象方法上传文件。
//		String[] upload_file = storageClient.upload_file("H:\\360downloads\\310350.jpg", "jpg", null);
		String[] upload_file = storageClient.upload_file("C:\\Users\\hujy\\Documents\\Tencent Files\\912889433\\Image\\Group\\1.jpg", "jpg", null);
		for (String string : upload_file) {
			System.out.println(string);
		}
	}
	
	@Test
	public void FastDFSClientTest() throws Exception {
		FastDFSClient fc = new FastDFSClient("D:\\code\\soa_taotao\\taotao-content\\taotao-content-service\\src\\main\\resources\\properties\\client.conf");
		String string = fc.uploadFile("C:\\Users\\hujy\\Documents\\Tencent Files\\912889433\\Image\\Group\\1.jpg", "jpg");
		System.out.println(string);
	}
}
