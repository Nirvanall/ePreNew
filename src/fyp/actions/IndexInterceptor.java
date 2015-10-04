package fyp.actions;

import com.opensymphony.xwork2.ActionSupport;

public class IndexInterceptor extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		return INPUT;
	}
}