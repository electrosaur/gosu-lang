/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.exceptions.IncompatibleTypeException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IGosuClassLoadingObserver;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.util.ILogger;
import gw.util.SystemOutLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 */
public class DefaultEntityAccess extends BaseService implements IEntityAccess
{
  public static final SystemOutLogger DEFAULT_LOGGER = new SystemOutLogger(SystemOutLogger.LoggingLevel.WARN);
  private static DefaultEntityAccess g_instance;
  private static final ITypeUsesMap EMPTY_TYPE_USES = new TypeUsesMap( Collections.emptyList() ).lock();
  private List<IGosuClassLoadingObserver> _classLoadingObservers;

  public static DefaultEntityAccess instance()
  {
    return g_instance == null ? g_instance = new DefaultEntityAccess() : g_instance;
  }

  /**
   */
  public DefaultEntityAccess()
  {
    _classLoadingObservers = Collections.synchronizedList( new ArrayList<>());
  }

  public ITypeLoader getDefaultTypeLoader()
  {
    return DefaultTypeLoader.instance();
  }

  @Override
  public ITypeUsesMap getDefaultTypeUses()
  {
    return EMPTY_TYPE_USES;
  }

  /**
   * @return x
   */
  @Override
  public boolean isDomainInstance( Object value )
  {
    return false;
  }

  /**
   * @return x
   */
  @Override
  public boolean isEntityClass( IType cls )
  {
    return false;
  }

  /**
   * @return x
   */
  @Override
  public Object getEntityInstanceFrom( Object entity, IType classEntity )
  {
    return null;
  }

  /**
   * @return x
   */
  @Override
  public boolean areBeansEqual( Object bean1, Object bean2 )
  {
    return bean1.equals( bean2 );
  }

  /**
   */
  @Override
  public boolean verifyValueForType( IType type, Object value ) throws RuntimeException
  {
    try
    {
      IType valueType = TypeLoaderAccess.instance().getIntrinsicTypeFromObject(value);
      CommonServices.getCoercionManager().verifyTypesComparable( type, valueType, false );
    }
    catch( ParseIssue pe )
    {
      throw new IncompatibleTypeException( "Value of type: " + TypeLoaderAccess.instance().getIntrinsicTypeFromObject( value ).getName() +
                                           " is not compatible with symbol type: " + type.getName() );
    }
    return true;
  }

  @Override
  public String makeStringFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    return obj.toString();
  }


  /**
   * @return x
   */
  @Override
  public long getHashedEntityId( String strId, IType classEntity )
  {
    return -1;
  }

  /**
   * @return x
   */
  @Override
  public boolean isInternal( IType cls )
  {
    return false;
  }

  @Override
  public ILogger getLogger()
  {
    return DEFAULT_LOGGER;
  }

  @Override
  public Locale getLocale()
  {
    return Locale.getDefault();
  }

  @Override
  public Date getCurrentTime()
  {
    return new Date();
  }

  @Override
  public TimeZone getTimeZone() {
    return TimeZone.getDefault();
  }

  @Override
  public void addEnhancementMethods(IType typeToEnhance, Collection methodsToAddTo)
  {
    if( GosuClassTypeLoader.getDefaultClassLoader() != null )
    {
      GosuClassTypeLoader.getDefaultClassLoader().getEnhancementIndex().addEnhancementMethods( typeToEnhance, methodsToAddTo);
    }
  }

  @Override
  public void addEnhancementProperties(IType typeToEnhance, Map propertyInfosToAddTo, boolean caseSensitive)
  {
    if( GosuClassTypeLoader.getDefaultClassLoader() != null )
    {
      GosuClassTypeLoader.getDefaultClassLoader().getEnhancementIndex().addEnhancementProperties( typeToEnhance, propertyInfosToAddTo, caseSensitive);
    }
  }

  @Override
  public ClassLoader getPluginClassLoader()
  {
    return DefaultEntityAccess.class.getClassLoader();
  }

  @Override
  public StringBuilder getPluginRepositories()
  {
    return new StringBuilder();
  }

  @Override
  public String getWebServerPaths()
  {
    return "";
  }

  @Override
  public boolean isUnreachableCodeDetectionOn()
  {
    return true;
  }

  @Override
  public IType getKeyType()
  {
    return null;
  }

  @Override
  public IPropertyInfo getEntityIdProperty( IType rootType )
  {
    return null;
  }

  @Override
  public boolean shouldAddWarning( IType type, IParseIssue warning )
  {
    return true;
  }

  @Override
  public ILanguageLevel getLanguageLevel()
  {
    return new StandardLanguageLevel();
  }

  @Override
  public List<IGosuClassLoadingObserver> getGosuClassLoadingObservers() {
    return _classLoadingObservers;
  }

  @Override
  public boolean areUsesStatementsAllowedInStatementLists(ICompilableType gosuClass) {
    return false;
  }

  @Override
  public List<IDirectory> getAdditionalSourceRoots() {
    return Collections.emptyList();
  }

  @Override
  public void reloadedTypes(String[] types) {
    //nothing to do
  }

  @Override
  public ExtendedTypeDataFactory getExtendedTypeDataFactory(String typeName) {
    return null;
  }

  @Override
  public String getLocalizedTypeName(IType type) {
    return type.getName();
  }

  @Override
  public String getLocalizedTypeInfoName(IType type) {
    String result;
    if (type instanceof IJavaType) {
      result = ((IJavaType) type).getBackingClassInfo().getDisplayName();
    } else {
      result = getLocalizedTypeName(type);
    }
    return result;
  }

}
