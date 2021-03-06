import java.io.*;
import java.util.*;

public class Test{
	public static String diffSideBySide(String fromStr, String toStr){
		// this is equivalent of running unix diff -y command
		// not pretty, but it works. Feel free to refactor against unit test.
		String[] fromLines = fromStr.split("\n");
		String[] toLines = toStr.split("\n");
		List<Difference> diffs = (new Diff(fromLines, toLines)).diff();

		int padding = 3;
		int maxStrWidth = Math.max(maxLength(fromLines), maxLength(toLines)) + padding;

		StrBuilder diffOut = new StrBuilder();
		diffOut.setNewLineText("\n");
		int fromLineNum = 0;
		int toLineNum = 0;
		for(Difference diff : diffs) {
			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();

			boolean isAdd = (delEnd == Difference.NONE && addEnd != Difference.NONE);
			boolean isDel = (addEnd == Difference.NONE && delEnd != Difference.NONE);
			boolean isMod = (delEnd != Difference.NONE && addEnd != Difference.NONE);

			//write out unchanged lines between diffs
			while(true) {
				String left = "";
				String right = "";
				if (fromLineNum < (delStart)){
					left = fromLines[fromLineNum];
					fromLineNum++;
				}
				if (toLineNum < (addStart)) {
					right = toLines[toLineNum];
					toLineNum++;
				}
				diffOut.append(StringUtils.rightPad(left, maxStrWidth));
				diffOut.append("  "); // no operator to display
				diffOut.appendln(right);

				if( (fromLineNum == (delStart)) && (toLineNum == (addStart))) {
					break;
				}
			}

			if (isDel) {
				//write out a deletion
				for(int i=delStart; i <= delEnd; i++) {
					diffOut.append(StringUtils.rightPad(fromLines[i], maxStrWidth));
					diffOut.appendln("<");
				}
				fromLineNum = delEnd + 1;
			} else if (isAdd) {
				//write out an addition
				for(int i=addStart; i <= addEnd; i++) {
					diffOut.append(StringUtils.rightPad("", maxStrWidth));
					diffOut.append("> ");
					diffOut.appendln(toLines[i]);
				}
				toLineNum = addEnd + 1; 
			} else if (isMod) {
				// write out a modification
				while(true){
					String left = "";
					String right = "";
					if (fromLineNum <= (delEnd)){
						left = fromLines[fromLineNum];
						fromLineNum++;
					}
					if (toLineNum <= (addEnd)) {
						right = toLines[toLineNum];
						toLineNum++;
					}
					diffOut.append(StringUtils.rightPad(left, maxStrWidth));
					diffOut.append("| ");
					diffOut.appendln(right);

					if( (fromLineNum > (delEnd)) && (toLineNum > (addEnd))) {
						break;
					}
				}
			}

		}

		//we've finished displaying the diffs, now we just need to run out all the remaining unchanged lines
		while(true) {
			String left = "";
			String right = "";
			if (fromLineNum < (fromLines.length)){
				left = fromLines[fromLineNum];
				fromLineNum++;
			}
			if (toLineNum < (toLines.length)) {
				right = toLines[toLineNum];
				toLineNum++;
			}
			diffOut.append(StringUtils.rightPad(left, maxStrWidth));
			diffOut.append("  "); // no operator to display
			diffOut.appendln(right);

			if( (fromLineNum == (fromLines.length)) && (toLineNum == (toLines.length))) {
				break;
			}
		}

		return diffOut.toString();
	}

	private static int maxLength(String[] fromLines) {
		int maxLength = 0;

		for (int i = 0; i < fromLines.length; i++) {
			if (fromLines[i].length() > maxLength) {
				maxLength = fromLines[i].length();
			}
		}
		return maxLength;
	}

	public static void main(String []args){
		String fromText="test 1,2,3,4,'\r', test 5,6,7,8,'\r',test 9,10,11,12,'\r',test 13,14,15,16";
		String toText="test 1,2,3,4,'\r',test 5,6,7,8,'\r',test 9,10,11,12,13,'\r', test 13,14,15,16";
		String res= diffSideBySide(fromText,toText);
	}
}