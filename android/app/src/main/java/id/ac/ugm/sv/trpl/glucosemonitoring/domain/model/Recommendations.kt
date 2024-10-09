package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.calculateBmi
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.calculateHealthyWeight
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.classifyBloodPressure
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.BloodPressureCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.TirStatus
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.WeightStatus

class Recommendations(builder: Builder) {
    
    val glucoseRecommendation: GlucoseRecommendation? =
        builder.glucoseRecommendation
    val bloodPressureRecommendation: BloodPressureRecommendation? =
        builder.bloodPressureRecommendation
    val bmiRecommendation: BmiRecommendation? =
        builder.bmiRecommendation
    val isEmpty: Boolean
        get() = glucoseRecommendation == null &&
                bloodPressureRecommendation == null &&
                bmiRecommendation == null
    
    class Builder {
        
        var glucoseRecommendation: GlucoseRecommendation? = null
        var bloodPressureRecommendation: BloodPressureRecommendation? = null
        var bmiRecommendation: BmiRecommendation? = null
        
        fun setTimeInRange(tir: Int?, tar: Int?, tbr: Int?): Builder {
            if (tir != null && tar != null && tbr != null) {
                val value = "$tir%"
                glucoseRecommendation = if (TirStatus.IN_RANGE.range.contains(tir)) {
                    NormoglycemiaRecommendation(
                        value = value,
                    )
                } else if (tar >= tbr) {
                    HyperglycemiaRecommendation(
                        value = value,
                    )
                } else {
                    HypoglycemiaRecommendation(
                        value = value,
                    )
                }
            }
            return this
        }
        
        fun setBloodPressure(systolic: Int?, diastolic: Int?): Builder {
            if (systolic != null && diastolic != null) {
                val category = classifyBloodPressure(systolic, diastolic)
                val value = "${systolic}/${diastolic} mmHg"
                
                bloodPressureRecommendation =
                    when (category) {
                        BloodPressureCategory.NORMAL -> NormotensionRecommendation(
                            value = value,
                            status = "Normal",
                        )
                        
                        BloodPressureCategory.ELEVATED -> NormotensionRecommendation(
                            value = value,
                            status = "Meningkat",
                        )
                        
                        BloodPressureCategory.HYPERTENSION_STAGE_1 -> HypertensionRecommendation(
                            value = value,
                            status = "Hipertensi tahap 1",
                        )
                        
                        BloodPressureCategory.HYPERTENSION_STAGE_2 -> HypertensionRecommendation(
                            value = value,
                            status = "Hipertensi tahap 2",
                        )
                        
                        BloodPressureCategory.HYPERTENSIVE_CRISIS -> HypertensiveCrisisRecommendation(
                            value = value,
                            status = "Krisis hipertensi",
                        )
                    }
            }
            return this
        }
        
        fun setBmi(weight: Int?, height: Int?): Builder {
            if (weight != null && height != null) {
                val bmi = calculateBmi(weight, height)
                val healthyWeight = calculateHealthyWeight(height)
                
                val value = "$bmi | $weight kg"
                val recommendedValue =
                    ("${WeightStatus.NORMAL_WEIGHT.range.start}—" +
                            "${WeightStatus.NORMAL_WEIGHT.range.endInclusive}")
                        .plus(" | ${healthyWeight.first}—${healthyWeight.last} kg")
                
                bmiRecommendation = when (WeightStatus.fromBmi(bmi)) {
                    WeightStatus.UNDERWEIGHT -> UnderweightRecommendation(
                        value = value,
                        status = "Berat badan rendah",
                        recommendedValue = recommendedValue,
                    )
                    
                    WeightStatus.NORMAL_WEIGHT -> NormalWeightRecommendation(
                        value = value,
                        status = "Berat badan normal",
                        recommendedValue = recommendedValue,
                    )
                    
                    WeightStatus.OVERWEIGHT -> OverweightRecommendation(
                        value = value,
                        status = "Berat badan berlebih",
                        recommendedValue = recommendedValue,
                    )
                    
                    WeightStatus.OBESITY_CLASS_1 -> OverweightRecommendation(
                        value = value,
                        status = "Obesitas kelas 1",
                        recommendedValue = recommendedValue,
                    )
                    
                    WeightStatus.OBESITY_CLASS_2 -> OverweightRecommendation(
                        value = value,
                        status = "Obesitas kelas 2",
                        recommendedValue = recommendedValue,
                    )
                    
                    WeightStatus.OBESITY_CLASS_3 -> OverweightRecommendation(
                        value = value,
                        status = "Obesitas kelas 3",
                        recommendedValue = recommendedValue,
                    )
                }
            }
            return this
        }
        
        fun provide(): Recommendations {
            return Recommendations(this)
        }
        
    }
    
    sealed interface Recommendation {
        
        val type: String
        val value: String
        val status: String
        val recommendedValue: String
        val meetTarget: Boolean
        
        fun getRecommendation(): String
        
    }
    
    sealed class GlucoseRecommendation : Recommendation {
        
        override val type: String = "Time in range"
        override val recommendedValue: String = "di atas 70%"
        
    }
    
    // Source: American Diabetes Association (ADA)
    class NormoglycemiaRecommendation(
        override val value: String,
    ) : GlucoseRecommendation() {
        
        override val status: String = "Memenuhi target"
        override val meetTarget: Boolean = true
        
        override fun getRecommendation(): String {
            return """
            Disarankan menjaga pola makan sehat melalui makanan utuh (belum diproses) atau olahan minimal (sedikit diproses), makan sayur nontepung ½ porsi, membatasi gula tambahan, dan membatasi lemak tak sehat (lemak jenuh dan lemak trans)/mengutamakan lemak sehat (lemak tak jenuh), menjaga berat badan sehat, melakukan olahraga sedang selama 150 menit/minggu yang dapat dilakukan dalam 3 hari dan maksimal jeda 2 hari, dapat melakukan olahraga tambahan: latihan kekuatan 2-3 sesi/minggu yang dilakukan dalam 1 hari atau beberapa hari secara berurutan, serta menghindari rokok dan minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    // Source: American Diabetes Association (ADA)
    class HyperglycemiaRecommendation(
        override val value: String,
    ) : GlucoseRecommendation() {
        
        override val status: String = "Di bawah target"
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan mengutamakan makanan utuh (belum diproses) atau olahan minimal (sedikit diproses) dan menambahkan sayuran nontepung ½ porsi.

            Untuk sumber karbohidrat ¼ porsi, dapat berasal dari buah-buahan, biji padi-padian utuh, sayuran bertepung, polong-polongan, dan lentil.
            
            Untuk sumber protein ¼ porsi, pilih protein bebas dari lemak tak sehat (lemak jenuh dan lemak trans), dapat berupa protein hewani (seperti daging tanpa lemak, telur, ikan, dan kerang-kerangan) dan protein nabati (seperti polong-polongan, lentil, dan kacang pohon).
            
            Batasi makanan dengan gula tambahan (seperti minuman manis), biji-bijian olahan (seperti nasi putih), dan permen juga camilan serta beralih dari bahan makanan yang tinggi lemak jenuh dengan tinggi lemak sehat (lemak tak jenuh; seperti mengganti minyak sawit dengan minyak zaitun).
            
            Disarankan melakukan latihan aerobik sedang dengan total 150 menit/minggu yang dapat dilakukan dalam 3 hari dan maksimal jeda 2 hari.
            
            Olahraga tambahan yang dapat dilakukan: latihan kekuatan 2-3 sesi/minggu yang dilakukan dalam 1 hari atau beberapa hari secara berurutan.
            
            Disarankan juga menjaga berat badan sehat melalui peningkatan gaya hidup, seperti mengubah pola makan dan lebih banyak bergerak, serta menghindari rokok dan minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    // Source: American Diabetes Association (ADA)
    class HypoglycemiaRecommendation(
        override val value: String,
    ) : GlucoseRecommendation() {
        
        override val status: String = "Di bawah target"
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan meningkatkan konsumsi karbohidrat secara cukup melalui makanan sehat (berupa makanan utuh/belum diproses atau olahan minimal/sedikit diproses dan bebas/rendah lemak tak sehat: lemak jenuh dan trans), mengatur secara tepat jadwal, intensitas, dan durasi olahraga, serta menghindari rokok dan minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    sealed class BloodPressureRecommendation : Recommendation {
        
        override val type: String = "Tekanan darah"
        override val recommendedValue: String = "di bawah 130/80 mmHg"
        
    }
    
    class NormotensionRecommendation(
        override val value: String,
        override val status: String,
    ) : BloodPressureRecommendation() {
        
        override val meetTarget: Boolean = true
        
        override fun getRecommendation(): String {
            return """
            Disarankan menjaga pola makan sehat dengan membatasi lemak tak sehat serta garam tambahan, menjaga berat badan melalui aktivitas fisik dan membatasi gula tambahan, melakukan latihan aerobik sedang 150 menit/minggu atau 75 menit/minggu jika intensitas berat, serta menghindari rokok dan minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    class HypertensionRecommendation(
        override val value: String,
        override val status: String,
    ) : BloodPressureRecommendation() {
        
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan menambahkan buah dan sayur pada setiap porsi, makan biji-bijian utuh yang berserat tinggi, mengutamakan pilihan lemak sehat dan dalam porsi kecil, memilih daging tanpa lemak dan kulit, menghindari memasak menggunakan banyak minyak, membatasi garam tambahan, dan memilih produk susu yang rendah lemak.

            Bandingkan antara beberapa jenis produk yang sama untuk mendapatkan produk dengan kandungan sodium/natrium (garam) paling sedikit.
            
            Disarankan membatasi minuman manis, permen, dan camilan asin, melakukan latihan aerobik sedang 150 menit/minggu atau 75 menit/minggu jika intensitas berat, serta menghindari rokok dan minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    class HypertensiveCrisisRecommendation(
        override val value: String,
        override val status: String,
    ) : BloodPressureRecommendation() {
        
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan segera memeriksakan diri ke dokter atau rumah sakit terdekat untuk pertolongan medis.
            """.trimIndent()
        }
        
    }
    
    sealed class BmiRecommendation : Recommendation {
        
        override val type: String = "BMI"
        
    }
    
    class UnderweightRecommendation(
        override val value: String,
        override val status: String,
        override val recommendedValue: String,
    ) : BmiRecommendation() {
        
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan mengonsumsi makanan sehat dan bervariasi yang padat nutrisi dengan mengutamakan makanan yang berasal tumbuhan, memilih sumber karbohidrat yang berasal dari roti, biji padi-padian utuh, pasta, nasi, atau kentang, makan sayur dan buah segar setiap hari (total minimal 400 gram), membatasi asupan lemak maksimal 30% porsi, menghindari lemak tak sehat, serta membatasi gula dan garam tambahan.

            Disarankan melakukan olahraga sedang selama 150 menit/minggu dan menghindari minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    class NormalWeightRecommendation(
        override val value: String,
        override val status: String,
        override val recommendedValue: String,
    ) : BmiRecommendation() {
        
        override val meetTarget: Boolean = true
        
        override fun getRecommendation(): String {
            return """
            Disarankan menjaga pola makan sehat dengan mengutamakan makanan yang berasal tumbuhan, makanan yang bervariasi, memilih sumber karbohidrat yang berasal dari roti, biji padi-padian utuh, pasta, nasi, atau kentang, makan sayur dan buah segar setiap hari (total minimal 400 gram), membatasi asupan lemak maksimal 30% porsi, menghindari lemak tak sehat, serta membatasi gula dan garam tambahan.

            Disarankan melakukan olahraga sedang selama 150 menit/minggu dan menghindari minuman alkohol.
            """.trimIndent()
        }
        
    }
    
    class OverweightRecommendation(
        override val value: String,
        override val status: String,
        override val recommendedValue: String,
    ) : BmiRecommendation() {
        
        override val meetTarget: Boolean = false
        
        override fun getRecommendation(): String {
            return """
            Disarankan menurunkan berat badan dengan lebih banyak bergerak, melakukan olahraga sedang selama 150 menit/minggu, dan membatasi gula dan garam tambahan, seperti minuman manis, permen, dan camilan asin.

            Disarankan mengonsumsi makanan sehat dan bervariasi, berupa makanan utuh atau olahan minimal dan bebas/rendah lemak tak sehat, mengutamakan makanan yang berasal dari tumbuhan, dan menghindari minuman alkohol.
            """.trimIndent()
        }
        
    }
    
}