package io.purplejs.servlet.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.io.ByteSource;
import com.google.common.net.MediaType;

import static org.junit.Assert.*;

public class MultipartItemImplTest
{
    private Part part;

    private MultipartItemImpl item;

    @Before
    public void setUp()
    {
        this.part = Mockito.mock( Part.class );
        this.item = new MultipartItemImpl( this.part );
    }

    @Test
    public void getName()
    {
        Mockito.when( this.part.getName() ).thenReturn( "item" );

        final String name = this.item.getName();
        assertEquals( "item", name );
    }

    @Test
    public void getFileName()
    {
        Mockito.when( this.part.getSubmittedFileName() ).thenReturn( "file.txt" );

        final String name = this.item.getFileName();
        assertEquals( "file.txt", name );
    }

    @Test
    public void getSize()
    {
        Mockito.when( this.part.getSize() ).thenReturn( 10L );

        final long size = this.item.getSize();
        assertEquals( 10L, size );
    }

    @Test
    public void getContentType()
    {
        Mockito.when( this.part.getContentType() ).thenReturn( "text/plain" );

        final MediaType type1 = this.item.getContentType();
        assertNotNull( type1 );
        assertEquals( "text/plain", type1.toString() );

        Mockito.when( this.part.getContentType() ).thenReturn( null );

        final MediaType type2 = this.item.getContentType();
        assertNull( type2 );
    }

    @Test
    public void getBytes()
        throws Exception
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( new byte[2] );
        Mockito.when( this.part.getInputStream() ).thenReturn( in );
        Mockito.when( this.part.getSize() ).thenReturn( 2L );

        final ByteSource value = this.item.getBytes();
        assertNotNull( value );
        assertEquals( 2L, value.size() );
    }

    @Test
    public void getAsString()
        throws Exception
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( "hello".getBytes() );
        Mockito.when( this.part.getInputStream() ).thenReturn( in );

        final String value = this.item.getAsString();
        assertNotNull( value );
        assertEquals( "hello", value );
    }

    @Test(expected = IOException.class)
    public void getAsString_error()
        throws Exception
    {
        Mockito.when( this.part.getInputStream() ).thenThrow( new IOException() );
        this.item.getAsString();
    }

    @Test
    public void delete()
        throws Exception
    {
        this.item.delete();
        Mockito.verify( this.part, Mockito.times( 1 ) ).delete();

        Mockito.doThrow( new IOException() ).when( this.part ).delete();
        this.item.delete();
    }
}
