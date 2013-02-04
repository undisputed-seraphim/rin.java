package rin.gl.lib3d;

import java.util.ArrayList;

import rin.util.Buffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class GLInterleavedBuffer {
	public static enum IndexType {
		VERTEX ( 0 ),
		COLOR ( 1 ),
		NORMAL ( 2 ),
		TEXCOORD ( 3 );
		
		protected int target;
		IndexType( int target ) {
			this.target = target;
		}
	}
	
	protected int				type =		GL_FLOAT,
								usage =		GL_STATIC_DRAW,
								stride =	0,
								buffer =	-1,
								target =	-1,
								count =		0;

	private ArrayList<Integer>	attribute =	new ArrayList<Integer>(),
								index =		new ArrayList<Integer>(),
								size =		new ArrayList<Integer>(),
								offset =	new ArrayList<Integer>();
	
	public GLInterleavedBuffer( int target, float[] data ) {
		this.target = target;
		this.bindData( data );
	}
	
	public GLInterleavedBuffer bindData( float[] arr ) {
		if( arr.length > 0 ) {
			this.buffer = glGenBuffers();
			glBindBuffer( this.target, this.buffer );
			glBufferData( this.target, Buffer.toBuffer( arr ), this.usage );
			glBindBuffer( this.target, 0 );
		}
		return this;
	}
	
	public GLInterleavedBuffer addIndex( IndexType index, int size, int attr ) {
		this.count++;
		this.index.add( index.target );
		this.attribute.add( attr );
		this.size.add( size );
		return this;
	}
	
	public GLInterleavedBuffer build() {
		this.offset.clear();
		this.stride = 0;
		if( this.buffer != -1 && this.count > 0 ) {
			for( int i = 0; i < this.count; i++ ) {
				this.offset.add( this.stride * 4 );
				this.stride += this.size.get( i );
			}
			this.stride *= 4;
		}
		return this;
	}
	
	public boolean buffer() {
		if( this.buffer != -1 && this.count > 0 ) {
			glBindBuffer( this.target, this.buffer );
			for( int i = 0; i < this.count; i++ ) {
				glVertexAttribPointer( this.attribute.get( i ), this.size.get( i ), this.type, false, this.stride, this.offset.get( i ) );
				glEnableVertexAttribArray( this.attribute.get( i ) );
			}
		}
		
		return false;
	}
	
	public GLInterleavedBuffer destroy() {
		if( this.buffer != -1 )
			glDeleteBuffers( Buffer.toBuffer( new int[]{ this.buffer } ) );
		
		return null;
	}
}
