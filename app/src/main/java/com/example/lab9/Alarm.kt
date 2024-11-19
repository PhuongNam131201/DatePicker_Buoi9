data class Alarm(
    var time: String,
    var date: String,
    var isChecked: Boolean = true // Thêm thuộc tính này để theo dõi trạng thái bật/tắt
)
