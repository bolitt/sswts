/**
 * urong@9#
 * 2007.07.29
 */

package org.net9.redbud.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class PermissionValidate {
	
	private static final String rootPosCode = "000000000000";
	private static final String categoryBossPosCode = "xxx101101101";
	private static final String categoryBossPosCode2 = "xxx000000000";
	private static final String organizationBossPosCode = "xxxxxx101101";
	private static final String departmentBossPosCode = "xxxxxxxxx101";
	
	/**
	 * 是否是Root， 岗位号为 000 000 000 000.
	 * @param posCode 岗位号
	 * @return 如果是root则返回true、否则返回false
	 */
	public static boolean isRoot(String posCode){
		return posCode.equals(rootPosCode);
	}
	
	/**
	 * 是否是CategoryBoss，岗位号为xxx101101101，xxx000000000
	 * @param posCode 岗位号
	 * @return 如果是CategoryBoss则返回true，否则返回false
	 */
	public static boolean isCategoryBoss(String posCode){
		return (!isRoot(posCode)) && 
			   (posCode.regionMatches(3, categoryBossPosCode, 3, 9) ||
			   posCode.regionMatches(3, categoryBossPosCode2, 3, 9));
	}
	
	/**
	 * 至少是CategoryBoss，岗位号xxx101101101，xxx000000000，000000000000
	 * @param posCode 岗位号
	 * @return 如果至少是CategoryBoss则返回true，否则返回false
	 */
	public static boolean isAtLeastCategoryBoss(String posCode){
		return (isRoot(posCode) ||
				isCategoryBoss(posCode));
	}
	
	/**
	 * 是否是至少是系级OrganizationBoss，岗位号为4xx101101101，4xx000000000，4xxxxx101101
	 * @param posCode 岗位号
	 * @return 如果是系级CategoryBoss则返回true，否则返回false
	 */
	public static boolean isXiJiAtLeastOrganBoss(String posCode){
		return (isCategoryBoss(posCode) || isOrganizationBoss(posCode)) && (posCode.charAt(0) == '4');
	}
	
	/**
	 * 是否是OrganizationBoss，岗位号为 xxx xxx 101 101.
	 * @param posCode 岗位号
	 * @return 如果是OrganizationBoss则返回true，否则返回false
	 */
	public static boolean isOrganizationBoss(String posCode){
		return ((!PermissionValidate.isCategoryBoss(posCode)) && 
			   posCode.regionMatches(6, organizationBossPosCode, 6, 6));
	}
	
	/**
	 * 至少是Organization的Boss，包括xxxxxx101101，xxx101101101，xxx000000000，000000000000
	 * @param posCode is position code
	 * @return 如果至少是Organization Boss则返回true，否则返回false
	 */
	public static boolean isAtLeastOrganBoss(String posCode){
		return (isRoot(posCode) ||
				posCode.regionMatches(3, categoryBossPosCode, 3, 9) ||
				posCode.regionMatches(3, categoryBossPosCode2, 3, 9) ||
				posCode.regionMatches(6, organizationBossPosCode, 6, 6));
	}
	
	/**
	 * Is DepartmentBoss, position code is xxx xxx xxx 101.
	 * @param posCode is position code
	 * @return If is DepartmentBoss then return true, else return false.
	 */
	public static boolean isDepartmentBoss(String posCode){
		return (!PermissionValidate.isCategoryBoss(posCode) &&
			   !PermissionValidate.isOrganizationBoss(posCode) &&
			   posCode.regionMatches(9, departmentBossPosCode, 9, 3));
	}
	
	/**
	 * 至少是Department的Boss，包括xxxxxxxxx101，xxxxxx101101，xxx101101101，xxx000000000，000000000000
	 * @param posCode is position code
	 * @return 如果至少是Department Boss则返回true，否则返回false
	 */
	public static boolean isAtLeastDepartmentBoss(String posCode){
		return (isAtLeastOrganBoss(posCode) ||
				isDepartmentBoss(posCode));
	}
	
	/**
	 * 判断position是否有管理另一个position的权限
	 * @param bossPosCode 管理者的position code
	 * @param pos 被测试的position code
	 * @return 如果有管理权限则返回true，否则返回false
	 */
	public static boolean hasPosManagementPermission(String bossPosCode, String pos){
		//如果pos不存在，则返回false
		//PostsDAO posDAO = new PostsDAO();
		//if(posDAO.findByFullcode(pos).size() == 0){
		//	return false;
		//}
		
		//if bossPosCode is root, then return true
		if(PermissionValidate.isRoot(bossPosCode)){
			return true;
		}
		
		//else
		if(bossPosCode.endsWith("101101101") || bossPosCode.endsWith("000000000")){
			if(bossPosCode.regionMatches(0, pos, 0, 3)){
				return true;
			}
			return false;
		}
		else if(bossPosCode.endsWith("101101")){
			if(bossPosCode.regionMatches(0, pos, 0, 6)){
				return true;
			}
			return false;
		}
		else if(bossPosCode.endsWith("101")){
			if(bossPosCode.regionMatches(0, pos, 0, 9)){
				return true;
			}
			return false;
		}
		else{			
			return false;
		}
	}
	
	/**
	 * 是否有发布培训的权力。有权力的包括root、CategoryBoss、OrganizationBoss
	 * @param posCode 岗位号
	 * @return 如果有发布培训的权力则返回true，否则返回false
	 */
	public static boolean hasPostTrainingPermission(String posCode){
		return (PermissionValidate.isOrganizationBoss(posCode) ||
		   	    PermissionValidate.isCategoryBoss(posCode)) ||
		   	    PermissionValidate.isRoot(posCode);
	}
	
	/**
	 * 是否有发布校级信息的权力。有权力的包括root、CategoryBoss
	 * @param posCode 岗位号
	 * @return 如果有发布校级信息的权力则返回true，否则返回false
	 */
	public static boolean hasPublicInfoPostPermission(String posCode){
		return PermissionValidate.isCategoryBoss(posCode) ||
			   PermissionValidate.isRoot(posCode);
	}
	
	/**
	 * 是否有向下级发布信息的权力。有权力的包括root、CategoryBoss、OrganizationBoss、DepartmentBoss
	 * @param posCode 岗位号
	 * @return 如果有向下级发布信息的权力则返回true，否则返回false
	 */
	public static boolean hasInternalInfoPostPermission(String posCode){
		return PermissionValidate.isCategoryBoss(posCode) ||
			   PermissionValidate.isOrganizationBoss(posCode) ||
			   PermissionValidate.isDepartmentBoss(posCode) ||
			   PermissionValidate.isRoot(posCode);
	}
	
	public static boolean ifUserHasPermission(ArrayList<String> posts, String permissionMethodName){
		
		
		
		Method[] methods = PermissionValidate.class.getMethods();
		int methodIndex = 0;
		for(methodIndex = 0; methodIndex < methods.length; methodIndex ++){
			if(permissionMethodName.equals(methods[methodIndex].getName())){
				break;
			}
		}
		if(methodIndex >= methods.length){
			return false;
		}
		
		Method permissionMethod = methods[methodIndex];
		
		if(permissionMethod.getReturnType()!=boolean.class){
			return false;
		}
		
		boolean result = false;
		
		//System.out.println("posts"+posts.toString());
		Iterator it = posts.iterator();
		
		while(it.hasNext())
		{
			String postcode = (String)it.next();
			//System.out.println("postcode:"+postcode);
			try
			{
				result = result || Boolean.parseBoolean(permissionMethod.invoke(null, postcode).toString());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
