import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *       Original code is in C++, I just ported it to java       *
 *          The original code is under the MIT License.          *
 * Original: https://gist.github.com/krisys/71acdef7a33ed40bd586 */
public class Swipe {
	static String WORDLIST = "wordlist.txt";
	static ArrayList<String> words = new ArrayList<String>();

	static boolean match(String path, String word) {
		int pl = path.length(), wl = word.length(), j=0;
		for(int i=0;i<pl;i++) {
			if(path.charAt(i) == word.charAt(j)) {
				j++;
			}
		}
		if(j < wl) {
			return false;
		}
		return true;
	}

	static int get_keyboard_row(char c) {
		String[] args = new String[]{"qwertyuiop", "asdfghjkl", "zxcvbnm"};
		ArrayList<String> keyboard = new ArrayList<String>(Arrays.asList(args));
		for(int i=0;i<keyboard.size(); i++) {
			int n = keyboard.get(i).length();
			for(int j=0; j < n; j++) {
				if(keyboard.get(i).charAt(j) == c) {
					return i;
				}
			}
		}
		return -1;
	}

	static int[] compress(int[] s) {
		int i = 0, j = 0, n = s.length;
		while(i < n) {
			if(s[i] != s[j]) {
				s[++j] = s[i];
			}
			i++;
		}
		int[] returner = new int[2]; //{s[0], s[0]+j+1};
		returner[0]=s[0];
		returner[1]=s[0]+j+1;
		return returner;
	}

	static int get_minimum_wordlength(String path) {
		int n = path.length();
		int[] row_numbers = new int[n];
		for(int i=0;i<n;i++) {
			row_numbers[i] = get_keyboard_row(path.charAt(i));
		}
		int min_length = compress(row_numbers).length -3;
		if(min_length < 0) {
			min_length = 0;
		}
		return min_length;
	}

	static ArrayList<String> get_suggestions(String path) {
		ArrayList<String> suggestions_v1 = new ArrayList<String>(), suggestions_v2 = new ArrayList<String>(), suggestions_v3 = new ArrayList<String>();
		int n = words.size();
		int pathlen = path.length();
		int len;
		for(int i=0;i<n;i++) {
			len = words.get(i).length();
			if(path.charAt(0) == words.get(i).charAt(0) && path.charAt(pathlen-1) == words.get(i).charAt(len-1)) {
				suggestions_v1.add(words.get(i));
			}
		}
		n = suggestions_v1.size();
		for(int i=0;i<n;i++) {
			if(match(path, suggestions_v1.get(i))) {
				suggestions_v2.add(suggestions_v1.get(i));
			}
		}
		int min_length = get_minimum_wordlength(path);
		n = suggestions_v2.size();
		for(int i=0;i<n;i++) {
			if(suggestions_v2.get(i).length() > min_length) {
				suggestions_v3.add(suggestions_v2.get(i));
			}
		}
		
		return suggestions_v3;
	}
	public static void readfile() {
		words.clear();
		InputStream ins = null;
		Reader r = null;
		BufferedReader br = null;
		try {
			String s;
			ins = new FileInputStream(WORDLIST);
			r = new InputStreamReader(ins, "UTF-8");
			br = new BufferedReader(r);
			while( (s = br.readLine()) != null) {
				words.add(s);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if (br != null) { try { br.close(); } catch(Throwable t) { } }
			if (r != null) { try { r.close(); } catch(Throwable t) { } }
			if (ins != null) { try { ins.close(); } catch(Throwable t) { } }
		}
	}

	public static void main(String[] args) {
		ArrayList<String> suggestions = new ArrayList<String>();
		readfile();
		System.out.println(get_suggestions("heqerqllo"));
		System.out.println(get_suggestions("qwertyuihgfcvbnjk"));
		System.out.println(get_suggestions("wertyuioiuytrtghjklkjhgfd"));
		System.out.println(get_suggestions("dfghjioijhgvcftyuioiuytr"));
		System.out.println(get_suggestions("aserfcvghjiuytedcftyuytre"));
		System.out.println(get_suggestions("asdfgrtyuijhvcvghuiklkjuytyuytre"));
		System.out.println(get_suggestions("mjuytfdsdftyuiuhgvc"));
		System.out.println(get_suggestions("vghjioiuhgvcxsasdvbhuiklkjhgfdsaserty"));
	}
}
