package dal
{
	import mx.events.CalendarLayoutChangeEvent;

	public class UsuarioDal extends GenericDal
	{
		public function UsuarioDal()
		{
			super();
			gateway.url = "${tableName}.php";
			fields = {<#list columns as column>'${column.columnName}':Number,</#list>};
			//fields = {'id_usuario':Number, 'nombre_usuario':String, 'contrasena':String};
		}
	}
}