package rin.util;

import java.util.ArrayList;

public class XML {
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	/* create an xml object given the string contents of an xml file */
	public XML( String contents ) {
		String lines[] = contents.split( "\n" );

		/* stack for keeping track of which tag is the current active tag */
		ArrayList<Tag> current = new ArrayList<Tag>();
		
		/* loop through the lines and parse all the tags */
		for( String line : lines ) {
			/* find out what tag this line is */
			line = line.trim().toLowerCase();
			int pos = line.indexOf(" ") != -1 ? line.indexOf(" ") : line.indexOf(">");
			String tag = line.substring( line.indexOf("<") + 1, pos );
			if( tag.indexOf(">") != -1 ) tag = tag.substring( 0, tag.indexOf(">") );
			if( tag.indexOf("/") != -1 ) tag = tag.substring( tag.indexOf("/") + 1 );
			
			/* parent of any further tags is most recently inserted tag */
			Tag parent = null;
			if( current.size() > 0 )
				parent = current.get( current.size() - 1 );
			
			/* if tag is opening xml tag */
			if( line.indexOf( "<?" ) != -1 ) {
				this.addAttributes( line );
				/* grab the opening xml tag attributes and give to this object */
			}
			
			/* if tag is either a closing tag or a single line tag */
			else if( line.indexOf( "</" ) != -1 || line.indexOf("/>") != -1 ) {
				
				/* line is a closing tag, remove the most recent from stack */
				if( line.indexOf(" ") == -1 && line.indexOf("</") == 0 ) {
					if( current.size() > 0 )
						current.remove( current.size() - 1 );
				}
				
				/* if tag is closed on same line */
				else if( line.indexOf( "</"+ tag +">" ) != -1 || line.indexOf( "/>" ) != -1 ) {
					tags.add( new Tag( tag, parent ) );
					this.tags.get( this.tags.size() - 1 ).parseAttributes( line );
					if( parent != null )
						parent.addChild( this.get( this.tags.size() - 1 ) );
				}
			}
			
			/* tag is at least a tag */
			else if( line.indexOf( ">" ) != -1 ) {
				tags.add( new Tag( tag, parent ) );
				this.tags.get( this.tags.size() - 1 ).parseAttributes( line );
				current.add( tags.get( tags.size() - 1 ) );
				if( parent != null )
					parent.addChild( this.get( this.tags.size() - 1 ) );
			}
		}
	}
	
	public Tag get( int index ) { if( tags.size() >= index + 1 ) return this.tags.get( index ); return null; }
	public Tag[] getTagsByName( String name ) {
		int count = this.count( name );
		if( count == 0 )
			return new Tag[0];
		
		Tag[] tags = new Tag[ this.count( name ) ];
		count = 0;
		for( Tag t : this.tags )
			if( t.getName().equals( name ) )
				tags[ count++ ] = t;
		return tags;
	}
	public Tag getTagsByAttribute( String tag, String attr, String val ) { return null; }
	public void addAttribute( String atr, String val ) { this.attributes.add( new Attribute( atr, val ) ); }
	public void addAttributes( String str ) {
		str = str.substring( 0, str.indexOf( ">" ) );
		String atr, val;
		while( str.indexOf( "=" ) != -1 ) {
			atr = str.substring( str.indexOf( " " )+1, str.indexOf( "=" ) );
			str = str.substring( str.indexOf( "\"" )+1 );
			val = str.substring( 0, str.indexOf( "\"" ) );
			str = str.substring( str.indexOf( "\"" )+1 );
			this.addAttribute( atr, val );
		}
	}
	public int count( String tag ) {
		if( tag.equals( "*" ) )
			return this.tags.size();
		
		int count = 0;
		for( Tag t : this.tags )
			if( t.getName().equals( tag ) )
				count++;
		return count;
	}
	
	public void printTree() {
		System.out.println( "   XML" );
		for( Tag t : this.tags ) {
			/* only print all children of the root tag */
			if( t.parent == null ) {
				String tab = "";
				ArrayList<Tag> stack = new ArrayList<Tag>();
				stack.add( t );
				while( stack.size() > 0 ) {
					Tag current = stack.remove( stack.size() - 1 );
					tab = "   |";
					Tag cparent = current;
					while( cparent.parent != null ) {
						cparent = cparent.parent;
						tab += "---";
					}
					String attr = "(attrs: " + current.attributes.size() + ")";
					String con = "[content: " + current.content.length() + " chars]";
					System.out.println( tab + current.name + " " + attr + " " + con );
					
					for( Tag i : current.children )
						stack.add( i );
				}
				return;
			}
		}
		System.out.println( "No nodes!" );
	}
	
	public class Tag {
		private String name = "", content = "";
		private Tag parent = null;
		private ArrayList<Tag> children = new ArrayList<Tag>();
		private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		public Tag( String name, Tag parent ) {
			//System.out.println( "added tag " + name + " with parent " + ( parent != null ? parent : "null" ) );
			this.name = name;
			this.parent = parent;
		}
		
		public String getName() { return this.name; }
		public String getContent() { return this.content; }
		public Tag getParent() { return this.parent; }
		public Attribute getAttribute( String name ) {
			for( Attribute a : this.attributes )
				if( a.getName().equals( name ) )
					return a; return null;
		}
		public Attribute[] getAttributes() {
			Attribute[] attrs = new Attribute[ this.attributes.size() ];
			int count = 0;
			for( Attribute a : this.attributes )
				attrs[count++] = a;
			return attrs;
		}
		
		/* return first child matching name */
		public Tag getChild( String name ) { Tag[] tmp = this.getChildren( name, true ); return tmp.length > 0 ? tmp[0] : null; }
		
		/* return all children matching name */
		public Tag[] getChildren( String name ) { return this.getChildren( name, false ); }
		public Tag[] getChildren( String name, boolean single ) {
			ArrayList<Tag> stack = new ArrayList<Tag>();
			ArrayList<Tag> res = new ArrayList<Tag>();
			for( Tag tag : this.children )
				stack.add( tag );
			
			while( stack.size() > 0 ) {
				Tag current = stack.remove( stack.size() - 1 );
				if( current.getName().equals( name ) ) {
					if( single )
						return new Tag[]{ current };
					res.add( current );
				}
				
				for( Tag tag : current.children )
					stack.add( tag );
			}
			
			Tag[] result = new Tag[res.size()];
			int count = 0;
			for( Tag tag : res )
				result[count++] = tag;

			return result;
		}
		public int addChild( Tag t ) { this.children.add( t ); return this.children.size() - 1; }
		public void addAttribute( String atr, String val ) { this.attributes.add( new Attribute( atr, val ) ); }
		public void parseAttributes( String str ) {
			String con;
			con = str.substring( 1 );
			if( con.indexOf( ">" ) != -1 && con.indexOf( "<" ) != -1 ) {
				con = con.substring( con.indexOf( ">" )+1, con.indexOf( "<" ) );
				this.content = con;
			}
			
			str = str.substring( 0, str.indexOf( ">" ) );
			String atr, val;
			while( str.indexOf( "=" ) != -1 ) {
				atr = str.substring( str.indexOf( " " )+1, str.indexOf( "=" ) );
				str = str.substring( str.indexOf( "\"" )+1 );
				val = str.substring( 0, str.indexOf( "\"" ) );
				str = str.substring( str.indexOf( "\"" )+1 );
				this.addAttribute( atr, val );
			}
		}
		
		/* debug function. with no arguments, outputs attributes. with 'true', also prints children */
		public void debug() { this.debug( false ); }
		public void debug( boolean full ) {
			String str = "Tag[ " + this.name + " ]:\n";
			
			str += "|---Attributes:\n";
			for( Attribute a : this.attributes )
				str += "|------" + a.getName() + "=" + a.getValue() +"\n";
			
			if( full ) {
				str += "|---Children:\n";
				ArrayList<Tag> stack = new ArrayList<Tag>();
				for( Tag t : this.children )
					stack.add( t );
			
				while( stack.size() > 0 ) {
					Tag cur = stack.remove( stack.size() - 1 );
					String tab = "|------";
					
					Tag cpar = cur;
					while( cpar.parent != this ) {
						cpar = cpar.parent;
						tab += "---";
					}
					
					str += tab + "Tag[ " + cur.getName() + " ]:\n";
					for( Tag t : cur.children )
						stack.add( t );
				}
			}
			
			System.out.println( str );
		}
	}
	
	public class Attribute {
		private String	name,
						value;

		public Attribute( String name, String value ) {
			//System.out.println( "added attr " + name + " w/ val " + value );
			this.name = name;
			this.value = value;
		}
		
		public String getName() { return this.name; }
		public String getValue() { return this.value; }
	}
}
