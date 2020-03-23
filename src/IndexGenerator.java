public class IndexGenerator {
  private int[] index;
  private int len;
  private int[] radix;
  private boolean started;

  public IndexGenerator(int[] radix) {
    this.radix = radix;
    this.len = radix.length;
    this.index = new int[this.len];
    this.started = false;
  }

  public boolean hasNext() {
    if (!this.started) return true;
    for (int i = 0; i <= len-1; i++) {
      if(this.index[i] <= this.radix[i]-2) return true;
    }
    return false;
  }

  public int[] next() {
    if (!this.started) {
      this.started = true;
      return this.index;
    }
    if (this.hasNext()) {
      increase(0);
      return this.index;
    } else {
      throw new RuntimeException();
    }
  }

  private void increase(int d) {
    if (this.index[d] <= this.radix[d]-2) {
      this.index[d]++;
    } else {
      this.index[d] = 0;
      this.increase(d + 1);
    }
  }

}
