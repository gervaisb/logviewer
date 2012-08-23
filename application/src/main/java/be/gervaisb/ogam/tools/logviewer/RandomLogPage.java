package be.gervaisb.ogam.tools.logviewer;

import java.util.Date;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.core.LogPage;
import be.gervaisb.ogam.tools.logviewer.core.LogEntry.Severity;

public class RandomLogPage implements LogPage {

	@Override
	public LogEntry next() {
		long delay = (long) ((Math.random()*1800)/**(Math.random()*10)*/);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {/**/}
		
		Severity severity = Severity.Trace;
		StackTraceElement[] trace = null;
		if ( delay%2==0 ) {
			severity = Severity.Info;
		} else if ( delay%3==0 ) {
			severity = Severity.Error;
			trace = new NullPointerException("Sample").getStackTrace();
		}
		
		return new LogEntry(new Date(), severity, "Random entry with a delay of "+delay+"ms.", trace);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
