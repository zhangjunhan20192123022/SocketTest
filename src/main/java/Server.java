import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server=new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("客户端信息：" + server.getLocalSocketAddress() + "p:" + server.getLocalPort());


        //等待客户端连接
        for (;;) {
            //拿到客户端
            Socket client = server.accept();
            //客户端构建异步线程
            ClientHeader clientHeader=new ClientHeader(client);
            //启动线程
            clientHeader.start();

        }
    }

    /**
     * 客户端的消息处理部分
     */
    private static class ClientHeader extends Thread{
        private Socket socket;
        private boolean flag=true;

        ClientHeader(Socket socket){
            this.socket=socket;

        }
        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接："+socket.getInetAddress()+"p:"+socket.getPort());
            try{
                //得到打印流，用于数据输出；服务端回送数据
                PrintStream SocketOutput = new PrintStream(socket.getOutputStream());
                //得到输入流，接受客户端数据
                BufferedReader SocketInput= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    //客户端拿到数据
                     String str=SocketInput.readLine();
                     if ("bye".equalsIgnoreCase(str)){
                         flag=false;
                         SocketOutput.println("bye");
                     }else{
                         //打印到屏幕,并回送数据长度
                         System.out.println(str);
                         SocketOutput.println("回送数据长度："+str.length());
                     }
                }
                while (flag);


                SocketInput.close();
                SocketOutput.close();


            }catch (Exception e){
                System.out.println("连接异常断开");
            }finally {
                try{
                    socket.close();
                }catch (IOException e){
                      e.printStackTrace();
                }
                System.out.println("客户端已关闭");
            }
        }
    }
}
