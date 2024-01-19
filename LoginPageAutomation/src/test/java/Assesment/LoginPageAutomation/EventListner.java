package Assesment.LoginPageAutomation;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class EventListner implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		Reporter.log(result.getName() + " Started...");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		Reporter.log(result.getName() + " Success...");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		Reporter.log(result.getName() + " ...Failed");
	}
	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		Reporter.log(result.getName() + " ...Failed with timeout");
	}
	@Override
	public void onFinish(ITestContext context) {
		Reporter.log(context.getName() + "...Finished");
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		Reporter.log(result.getName() + "...Skipped");
	  }
}
