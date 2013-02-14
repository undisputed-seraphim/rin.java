package rin.gl.lib3d.data;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import rin.util.Buffer;

public class GLBuffer {
	protected int buffer = -1;
	protected int target = -1;
	protected int type = -1;
	
	protected int size = 0;
	protected int count = 0;
	protected int stride = 0;
	protected int offset = 0;
	
	protected int attribute = -1;
	protected int usage = GL_STATIC_DRAW;
	
	public GLBuffer( int target, float[] arr, int size, int stride, int offset ) { this( target, arr, size, stride, offset, -1 ); }
	public GLBuffer( int target, float[] arr, int size, int stride, int offset, int attr ) {
		this.target = target;
		this.type = GL_FLOAT;
		this.size = size;
		this.stride = stride;
		this.offset = offset;
		this.attribute = attr;
		this.bindData( arr );
	}
	
	public GLBuffer( int target, int[] arr ) {
		this.target = target;
		this.type = GL_UNSIGNED_INT;
		if( this.target == GL_ELEMENT_ARRAY_BUFFER )
			this.count = arr.length;
		this.bindData( arr );
	}
	
	public GLBuffer bindData( float[] arr ) {
		if( arr.length > 0 ) {
			this.buffer = glGenBuffers();
			glBindBuffer( this.target, this.buffer );
			glBufferData( this.target, Buffer.toBuffer( arr ), this.usage );
			glBindBuffer( this.target, 0 );
		}
		return this;
	}
	
	public GLBuffer bindData( int[] arr ) {
		if( arr.length > 0 ) {
			this.buffer = glGenBuffers();
			glBindBuffer( this.target, this.buffer );
			glBufferData( this.target, Buffer.toBuffer( arr ), this.usage );
			glBindBuffer( this.target, 0 );
		}
		return this;
	}
	
	public boolean buffer() {
		if( this.buffer != -1 ) {
			glBindBuffer( this.target, this.buffer );
			
			if( this.target == GL_ELEMENT_ARRAY_BUFFER )
				return true;
			
			if( this.attribute != -1 ) {
				glVertexAttribPointer( this.attribute, this.size, this.type, false, this.stride, this.offset );
				glEnableVertexAttribArray( this.attribute );
				return true;
			}
		}
		
		if( this.attribute != -1 )
			glDisableVertexAttribArray( this.attribute );
		
		return false;
	}
	
	public GLBuffer destroy() {
		if( this.buffer != -1 )
			glDeleteBuffers( Buffer.toBuffer( new int[]{ this.buffer } ) );
		
		return null;
	}
}
