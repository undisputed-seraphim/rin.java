package rin.gl.lib3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

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
	
	public GLBuffer( int target, float[] arr ) {
		this.target = target;
		this.type = GL_FLOAT;
		this.bindData( arr );
	}
	
	public GLBuffer( int target, int[] arr ) {
		this.target = target;
		this.type = GL_UNSIGNED_INT;
		if( this.target == GL_ELEMENT_ARRAY_BUFFER )
			this.count = arr.length;
		this.bindData( arr );
	}
	
	public GLBuffer setSSO( int size, int stride, int offset ) {
		this.size = size;
		this.stride = stride;
		this.offset = offset;
		return this;
	}
	
	public GLBuffer setAttribute( int attr ) {
		this.attribute = attr;
		return this;
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
	
	public void buffer() {
		if( this.buffer != -1 ) {
			glBindBuffer( this.target, this.buffer );
			
			if( this.target == GL_ELEMENT_ARRAY_BUFFER )
				return;
			
			if( this.attribute != -1 ) {
				glVertexAttribPointer( this.attribute, this.size, this.type, false, this.stride, this.offset );
				glEnableVertexAttribArray( this.attribute );
				return;
			}
		}
		
		if( this.attribute != -1 )
			glDisableVertexAttribArray( this.attribute );
	}
}
