/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.metamodel.source.internal.hbm;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.CascadeStyles;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.reflite.spi.JavaTypeDescriptor;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbColumnElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbKeyManyToOneElement;
import org.hibernate.metamodel.source.spi.AttributeSourceContainer;
import org.hibernate.metamodel.source.spi.RelationalValueSource;
import org.hibernate.metamodel.source.spi.SingularAttributeSource;
import org.hibernate.metamodel.source.spi.ToolingHintSource;
import org.hibernate.metamodel.spi.AttributePath;
import org.hibernate.metamodel.spi.AttributeRole;
import org.hibernate.metamodel.spi.NaturalIdMutability;
import org.hibernate.metamodel.spi.SingularAttributeNature;
import org.hibernate.type.ForeignKeyDirection;

/**
 * Implementation for {@code <key-many-to-one/>} mappings
 *
 * @author Gail Badner
 */
class KeyManyToOneSourceImpl
		extends AbstractToOneAttributeSourceImpl
		implements SingularAttributeSource {
	private final JaxbKeyManyToOneElement keyManyToOneElement;
	private final HibernateTypeSourceImpl typeSource;
	private final List<RelationalValueSource> valueSources;

	private final AttributePath attributePath;
	private final AttributeRole attributeRole;

	public KeyManyToOneSourceImpl(
			MappingDocument mappingDocument,
			AttributeSourceContainer container,
			final JaxbKeyManyToOneElement keyManyToOneElement,
			final NaturalIdMutability naturalIdMutability) {
		super( mappingDocument, naturalIdMutability, null );
		this.keyManyToOneElement = keyManyToOneElement;

		final String referencedClassName = keyManyToOneElement.getClazz();
		JavaTypeDescriptor referencedClass = null;
		if ( StringHelper.isNotEmpty( referencedClassName ) ) {
			referencedClass = bindingContext().getJavaTypeDescriptorRepository().getType(
					bindingContext().getJavaTypeDescriptorRepository().buildName(
							bindingContext().qualifyClassName( keyManyToOneElement.getClazz() )
					)
			);
		}
		this.typeSource = new HibernateTypeSourceImpl( referencedClass );

		this.valueSources = Helper.buildValueSources(
				sourceMappingDocument(),
				new Helper.ValueSourcesAdapter() {
					@Override
					public String getColumnAttribute() {
						return keyManyToOneElement.getColumnAttribute();
					}

					@Override
					public String getFormulaAttribute() {
						return null;
					}

					@Override
					public List<JaxbColumnElement> getColumn() {
						return keyManyToOneElement.getColumn();
					}

					@Override
					public List<String> getFormula() {
						return Collections.emptyList();
					}

					@Override
					public String getContainingTableName() {
						return null;
					}

					@Override
					public boolean isIncludedInInsertByDefault() {
						return true;
					}

					@Override
					public boolean isIncludedInUpdateByDefault() {
						return false;
					}
				}
		);

		this.attributePath = container.getAttributePathBase().append( getName() );
		this.attributeRole = container.getAttributeRoleBase().append( getName() );
	}

	@Override
	public String getName() {
		return keyManyToOneElement.getName();
	}

	@Override
	public AttributePath getAttributePath() {
		return attributePath;
	}

	@Override
	public AttributeRole getAttributeRole() {
		return attributeRole;
	}

	@Override
	public HibernateTypeSourceImpl getTypeInformation() {
		return typeSource;
	}

	@Override
	public String getPropertyAccessorName() {
		return keyManyToOneElement.getAccess();
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return false;
	}

	@Override
	public SingularAttributeNature getSingularAttributeNature() {
		return SingularAttributeNature.MANY_TO_ONE;
	}

	@Override
	public boolean isVirtualAttribute() {
		return false;
	}

	@Override
	protected boolean requiresImmediateFetch() {
		return false;
	}

	@Override
	protected String getFetchSelectionString() {
		return null;
	}

	@Override
	protected String getLazySelectionString() {
		return keyManyToOneElement.getLazy() != null ?
				keyManyToOneElement.getLazy().value() :
				null;
	}

	@Override
	protected String getOuterJoinSelectionString() {
		return null;
	}

	@Override
	public boolean areValuesIncludedInInsertByDefault() {
		return true;
	}

	@Override
	public boolean areValuesIncludedInUpdateByDefault() {
		return true;
	}

	@Override
	public boolean areValuesNullableByDefault() {
		return false;
	}

	@Override
	public String getContainingTableName() {
		return null;
	}

	@Override
	public List<RelationalValueSource> relationalValueSources() {
		return valueSources;
	}

	@Override
	public Collection<? extends ToolingHintSource> getToolingHintSources() {
		return keyManyToOneElement.getMeta();
	}

	@Override
	public String getReferencedEntityName() {
		return keyManyToOneElement.getEntityName();
	}

	@Override
	public boolean isUnique() {
		return false;
	}

	@Override
	public ForeignKeyDirection getForeignKeyDirection() {
		return ForeignKeyDirection.TO_PARENT;
	}

	@Override
	public Set<CascadeStyle> getCascadeStyles() {
		return Collections.singleton( CascadeStyles.NONE );
	}

	@Override
	public String getExplicitForeignKeyName() {
		return keyManyToOneElement.getForeignKey();
	}

	@Override
	public boolean isCascadeDeleteEnabled() {
		return "cascade".equals( keyManyToOneElement.getOnDelete().value() );
	}

	protected String getClassName() {
		return keyManyToOneElement.getClazz();
	}
}
