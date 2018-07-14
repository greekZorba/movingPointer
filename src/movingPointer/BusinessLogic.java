package movingPointer;

import java.util.concurrent.TimeUnit;

public class BusinessLogic {

	public String up(String beforeTime) {
		int ascendTime = (int)Double.parseDouble(beforeTime) + 1;
		
		return String.valueOf(ascendTime);
	}
	
	public String down(String beforeTime) {
		int dscendTime = (int)Double.parseDouble(beforeTime) - 1;
		
		/* 0 이하는 안되도록 방지 */
		return dscendTime < 0 ? "0" : String.valueOf(dscendTime);
	}
	
}
