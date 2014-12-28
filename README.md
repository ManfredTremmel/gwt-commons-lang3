gwt-commons-lang3
=================

A compatible GWT port of the apache commons lang3, which provides most of the functionality also on client (browser) side.

The build packages are currently not on a indexed maven repository, but you can add my private repository with the following settings:

```
  <repositories>
    <repository>    
      <id>de.knightsoft-net</id>    
      <url>http://www.knightsoft-net.de/maven/</url>    
      <snapshots>      
        <enabled>true</enabled> 
      </snapshots>    
      <releases>      
        <enabled>true</enabled>
      </releases>  
    </repository>
  </repositories>
```

The Package itself has to be includes as binary and source package. I've done it this way, to give you the possibility to take the original apache commons binary package (full compatible) and combine it with the gwt-commons version of the source package for the gwt compiler, this is useful, if you do have the binary package already in the dependency list.

```
    <dependency>
      <groupId>gwt-commons-lang3</groupId>
      <artifactId>gwt-commons-lang3</artifactId>
      <version>3.3.2-SNAPSHOT</version>
    </dependency>
    <dependency>
      <groupId>gwt-commons-lang3</groupId>
      <artifactId>gwt-commons-lang3</artifactId>
      <version>3.3.2-SNAPSHOT</version>
      <classifier>sources</classifier>
      <scope>provided</scope>
    </dependency>
```

GWT Integration
---------------

Add this inherit command into your project .gwt.xml file:

```
<inherits name="org.apache.commons.GWTCommonsLang3" />
```
