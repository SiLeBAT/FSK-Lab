Example code

```java
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			 TODO: find any available port and print it to sdout
			Server server = new Server(8000);

			try {
				server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class);
				server.setHandler(new HelloHandler());
				server.start();
				server.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	new Thread(runnable).start();
}

private static class HelloHandler extends AbstractHandler {
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello World</h1>");
	}
}
```

# Dependencies
- org.eclipse.jetty.http
- org.eclipse.jetty.io
- org.eclipse.jetty.server
- org.eclipse.jetty.util
- javax.servlet
